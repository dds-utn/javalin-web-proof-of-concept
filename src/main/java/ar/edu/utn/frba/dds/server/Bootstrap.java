package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.model.Noticia;
import ar.edu.utn.frba.dds.repositories.NoticiaRepositorio;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class Bootstrap {
    public static final String JWT_SECRET = "secreto-noticias-api-dds-utn";

    public static void init() {
        NoticiaRepositorio repositorio = NoticiaRepositorio.INSTANCE;

        repositorio.registrar(new Noticia(
            "Cuándo es la 5° Marcha Federal Universitaria: la fecha confirmada y los motivos del reclamo",
            "La nueva Marcha Federal Universitaria ya tiene fecha y lugar confirmados. Está convocada para el 15 de octubre y tendrá como ejes el reclamo por el financiamiento de las universidades públicas, la recomposición salarial y la aplicación de la Ley de Financiamiento Universitario.",
            "Redacción"));
        repositorio.registrar(new Noticia(
            "Misiones vuelve a habilitar el glifosato para fumigaciones: el agrotóxico señalado como cancerígeno regresa a la principal zona yerbatera del país",
            "Argentina. Misiones vuelve a habilitar el glifosato para fumigaciones: el agrotóxico señalado como cancerigeno regresa a la principal zona yerbatera del país.",
            "Redacción"));

        Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
        String apiKeyAdmin = JWT.create().withClaim("usuario", "admin").sign(algorithm);
        String apiKeyEditor = JWT.create().withClaim("usuario", "editor").sign(algorithm);

        System.out.println("=== API Keys generadas ===");
        System.out.println("admin:  " + apiKeyAdmin);
        System.out.println("editor: " + apiKeyEditor);
        System.out.println("==========================");
    }
}
