# Portal de Noticias - API

Requiere Java 17 y Maven 3.8.1 o superior.

> [Ver apunte](https://docs.google.com/document/d/1rSu56425FuLbIsJBN-ZzYlXmsdGvIbdV0KXeuuqWsI4/edit?tab=t.0)

## Iniciar el servidor

```
mvn compile exec:java -Dexec.mainClass="ar.edu.utn.frba.dds.server.App"
```

El servidor arranca en `http://localhost:9001`. Al iniciar, imprime por consola las API keys JWT necesarias para los endpoints protegidos:

```
=== API Keys generadas ===
admin:  <token>
editor: <token>
==========================
```

Usar el token en el header `Authorization: Bearer <token>` para las operaciones de escritura (POST, PUT, DELETE).

### Endpoints disponibles

| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| GET | `/api/noticias` | No | Lista todas las noticias |
| GET | `/api/noticias/:id` | No | Consulta una noticia por ID |
| POST | `/api/noticias` | Sí | Publica una noticia nueva |
| PUT | `/api/noticias/:id` | Sí | Actualiza título y contenido de una noticia |
| DELETE | `/api/noticias/:id` | Sí | Retracta (elimina) una noticia |
| POST | `/api/noticias/:id/comentarios` | Sí | Agrega un comentario a una noticia |
| GET | `/api/noticias/:id/comentarios/eventos` | No | Stream SSE de comentarios nuevos |
| POST | `/api/noticias/webhooks` | Solo admin | Registra un webhook para nuevas noticias |

### Negociación de contenido

Todos los endpoints soportan JSON (por defecto) y Protocol Buffers. Usar el header `Accept` para seleccionar el formato:

| `Accept` | Formato de respuesta |
|----------|----------------------|
| `application/json` (o ausente) | JSON |
| `application/x-protobuf` | Protocol Buffers binario |

Las estructuras protobuf son las mismas definidas en `src/main/proto/noticias.proto`. El endpoint `GET /api/noticias` responde con `ListarNoticiasResponse`; el resto responde con `Noticia` o `Comentario` según corresponda.

### Ejemplos con curl

Reemplazar `<token>` con una de las API keys que imprime el servidor al arrancar.

#### Listar todas las noticias
```bash
curl http://localhost:9001/api/noticias
```

#### Consultar una noticia
```bash
curl http://localhost:9001/api/noticias/1
```

#### Publicar una noticia
```bash
curl -X POST http://localhost:9001/api/noticias \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Título de ejemplo", "contenido": "Cuerpo de la noticia.", "autor": "Redacción"}'
```

#### Actualizar una noticia
```bash
curl -X PUT http://localhost:9001/api/noticias/1 \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Título corregido", "contenido": "Contenido actualizado."}'
```

#### Retractar una noticia
```bash
curl -X DELETE http://localhost:9001/api/noticias/1 \
  -H "Authorization: Bearer <token>"
```

#### Comentar una noticia
```bash
curl -X POST http://localhost:9001/api/noticias/1/comentarios \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"autor": "Lector", "contenido": "Muy buena nota."}'
```

#### Registrar un webhook

Registra una URL que recibirá un `POST` con el cuerpo de la noticia en JSON cada vez que se publique una nueva. Solo el token `admin` puede registrar webhooks.

```bash
# nota: para la URL se puede crear un servidor adicional o crear 
# una por ejemplo en https://webhook.site/
curl -X POST http://localhost:9001/api/noticias/webhooks \
  -H "Authorization: Bearer <token-admin>" \
  -H "Content-Type: application/json" \
  -d '{"url": "<URL>"}' 
```

Payload que recibe el webhook al publicarse una noticia:

```json
{
  "id": 3,
  "titulo": "Título de ejemplo",
  "contenido": "Cuerpo de la noticia.",
  "autor": "Redacción",
  "fechaPublicacion": "2026-10-03T12:00:00",
  "retractada": false,
  "comentarios": []
}
```

El envío es asíncrono con timeout de 5 segundos. Si el endpoint no responde, el error se ignora silenciosamente.

#### Seguir comentarios en tiempo real (SSE)

Abre una conexión `text/event-stream` que permanece abierta. Cada comentario nuevo llega como un evento nombrado:

```
event: comentario
data: {"id":1,"autor":"Lector","contenido":"Muy buena nota."}
```

El servidor envía un comentario SSE de keep-alive cada ~30 segundos para mantener la conexión activa a través de proxies:

```
: keep-alive
```

```bash
curl -iN -H "Accept: text/event-stream"  http://localhost:9001/api/noticias/1/comentarios/eventos
```

```
HTTP/1.1 200 OK
Date: Sat, 03 Oct 2026 21:57:13 GMT
Content-Type: text/event-stream;charset=utf-8
Connection: close
Cache-Control: no-cache
X-Accel-Buffering: no

: hello
event: comentario
data: {"autor":"Lector","contenido":"Muy buena nota.","id":1}

: keep-alive
```


#### Consultar una noticia en formato protobuf
```bash
curl -H "Accept: application/x-protobuf" http://localhost:9001/api/noticias/1 --output noticia.bin
```

Para decodificar la respuesta binaria con `protoc`:
```bash
curl -s -H "Accept: application/x-protobuf" http://localhost:9001/api/noticias/1 \
  | protoc --decode=portal.noticias.v1.Noticia src/main/proto/noticias.proto
```

#### Listar noticias en formato protobuf
```bash
curl -s -H "Accept: application/x-protobuf" http://localhost:9001/api/noticias \
  | protoc --decode=portal.noticias.v1.ListarNoticiasResponse src/main/proto/noticias.proto
```

## Servidor gRPC

El servidor gRPC arranca automáticamente junto con el HTTP, en el puerto **9002**.

El servicio expone los mismos casos de uso que la API REST y además permite suscribirse a los comentarios nuevos de una noticia en tiempo real mediante server-side streaming.

### Métodos disponibles

| RPC | Tipo | Descripción |
|-----|------|-------------|
| `ConsultarNoticia` | unario | Devuelve una noticia por ID |
| `PublicarNoticia` | unario | Crea una noticia nueva |
| `ActualizarNoticia` | unario | Actualiza título y cuerpo |
| `RetractarNoticia` | unario | Marca la noticia como retractada y la devuelve |
| `ComentarNoticia` | unario | Agrega un comentario a una noticia |
| `SeguirComentarios` | server-streaming | Recibe en tiempo real los comentarios nuevos de una noticia |

### Ejemplos con grpcurl

Instalar `grpcurl`: ver instrucciones en https://github.com/fullstorydev/grpcurl.

El servidor tiene habilitada la reflexión, así que no hace falta especificar el archivo `.proto`.

Los métodos de escritura requieren autenticación. Copiar el token que imprime el servidor al arrancar y guardarlo en una variable:

```bash
TOKEN=<token impreso al arrancar>
```

#### Listar los servicios disponibles
```bash
grpcurl -plaintext localhost:9002 list
```

#### Consultar una noticia (sin auth)
```bash
grpcurl -plaintext -d '{"id": 1}' localhost:9002 portal.noticias.v1.NoticiasService/ConsultarNoticia
```

#### Publicar una noticia (requiere auth)
```bash
grpcurl -plaintext \
  -H "authorization: Bearer $TOKEN" \
  -d '{"titulo": "Título de ejemplo", "cuerpo": "Cuerpo de la noticia.", "autor": "Redacción"}' \
  localhost:9002 portal.noticias.v1.NoticiasService/PublicarNoticia
```

#### Actualizar una noticia (requiere auth)
```bash
grpcurl -plaintext \
  -H "authorization: Bearer $TOKEN" \
  -d '{"id": 1, "titulo": "Título corregido", "cuerpo": "Contenido actualizado."}' \
  localhost:9002 portal.noticias.v1.NoticiasService/ActualizarNoticia
```

#### Retractar una noticia (requiere auth)
```bash
grpcurl -plaintext \
  -H "authorization: Bearer $TOKEN" \
  -d '{"id": 1}' \
  localhost:9002 portal.noticias.v1.NoticiasService/RetractarNoticia
```

#### Comentar una noticia (requiere auth)
```bash
grpcurl -plaintext \
  -H "authorization: Bearer $TOKEN" \
  -d '{"noticia_id": 1, "autor": "Lector", "texto": "Muy buena nota."}' \
  localhost:9002 portal.noticias.v1.NoticiasService/ComentarNoticia
```

#### Suscribirse a comentarios en tiempo real (sin auth, streaming, el proceso queda abierto)
```bash
grpcurl -plaintext -d '{"noticia_id": 1}' localhost:9002 portal.noticias.v1.NoticiasService/SeguirComentarios
```

Mientras ese proceso está abierto, cada nuevo comentario publicado (tanto por HTTP como por gRPC) aparecerá en la salida.
