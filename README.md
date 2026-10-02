# java-base-project

Esta es una plantilla de proyecto diseñada para: 

* Java 17. :warning: Si bien el proyecto no lo limita explícitamente, el comando `mvn verify` no funcionará con versiones más antiguas de Java. 
* JUnit 5. :warning: La versión 5 de JUnit es la más nueva del framework y presenta algunas diferencias respecto a la versión "clásica" (JUnit 4). Para mayores detalles, ver: 
  *  [Apunte de herramientas](https://docs.google.com/document/d/1VYBey56M0UU6C0689hAClAvF9ILE6E7nKIuOqrRJnWQ/edit#heading=h.dnwhvummp994)
  *  [Entrada de Blog (en inglés)](https://www.baeldung.com/junit-5-migration) 
  *  [Entrada de Blog (en español)](https://www.paradigmadigital.com/dev/nos-espera-junit-5/)
* Maven 3.8.1 o superior

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

### Ejemplos con curl

Reemplazar `<token>` con una de las API keys que imprime el servidor al arrancar.

**Listar todas las noticias**
```bash
curl http://localhost:9001/api/noticias
```

**Consultar una noticia**
```bash
curl http://localhost:9001/api/noticias/1
```

**Publicar una noticia**
```bash
curl -X POST http://localhost:9001/api/noticias \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Título de ejemplo", "contenido": "Cuerpo de la noticia.", "autor": "Redacción"}'
```

**Actualizar una noticia**
```bash
curl -X PUT http://localhost:9001/api/noticias/1 \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Título corregido", "contenido": "Contenido actualizado."}'
```

**Retractar una noticia**
```bash
curl -X DELETE http://localhost:9001/api/noticias/1 \
  -H "Authorization: Bearer <token>"
```

**Comentar una noticia**
```bash
curl -X POST http://localhost:9001/api/noticias/1/comentarios \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"autor": "Lector", "contenido": "Muy buena nota."}'
```

## Ejecutar tests

```
mvn test
```

## Validar el proyecto de forma exahustiva

```
mvn clean verify
```

Este comando hará lo siguiente:

 1. Ejecutará los tests
 2. Validará las convenciones de formato mediante checkstyle
 3. Detectará la presencia de (ciertos) code smells
 4. Validará la cobertura del proyecto

## Entrega del proyecto

Para entregar el proyecto, crear un tag llamado `entrega-final`. Es importante que antes de realizarlo se corra la validación
explicada en el punto anterior. Se recomienda hacerlo de la siguiente forma:

```
mvn clean verify && git tag entrega-final && git push origin HEAD --tags
```

## Configuración del IDE (IntelliJ)

### Usar el SDK de Java 17

1. En **File/Project Structure...**, ir a **Project Settings | Project**
2. En **Project SDK** seleccionar la versión 17 y en **Project language level** seleccionar `17 - Sealed types, always-strict floating-point semantics`

![image](https://user-images.githubusercontent.com/39303639/228126065-221b9851-fb96-4f7f-a8e1-010732dc7ef6.png)

### Usar fin de linea unix
1. En **File/Settings...**, ir a **Editor | Code Style**.
2. En la lista **Line separator**, seleccionar `Unix and OS X (\n)`.

![image](https://user-images.githubusercontent.com/39303639/228126546-352289fa-8feb-4b39-99db-d8b860915fea.png)

### Tabular con dos espacios

1. En **File/Settings...**, ir a **Editor | Code Style | Java | Tabs and Indents**.
2. Cambiar **Tab size**, **Indent** y **Continuation indent** a 2, 2 y 4 respectivamente:

![image](https://user-images.githubusercontent.com/39303639/228127009-8c84ea72-969b-4e05-b311-45e3688a4164.png)

### Ordenar los imports

1. En **File/Settings...**, ir a **Editor | Code Style | Java | Imports**.
2. Cambiar **Class count to use import with '*'** y **Names count to use static import with '*'** a un número muy alto (ej: 99).
3. En **Import Layout**, dejarlo como se muestra a continuación:
    - `import static all other imports`
    - `<blank line>`
    - `import all other imports`

![image](https://user-images.githubusercontent.com/39303639/228126787-36f9ecff-27f2-4b99-bf11-a6bd89f67087.png)

### Instalar y configurar Checkstyle

1. Instalar el plugin https://plugins.jetbrains.com/plugin/1065-checkstyle-idea:
2. En **File/Settings...**, ir a **Tools | Checkstyle**.
3. Configurarlo activando los Checks de Google y la versión de Checkstyle `== 9.0.1`:

![image](https://github.com/dds-utn/java-base-project/assets/11719816/b1edc122-4675-4f8d-bffc-9e3d3366fac6)

