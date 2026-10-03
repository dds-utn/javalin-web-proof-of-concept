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
        "Cuándo es la 5° Marcha Federal Universitaria: la fecha confirmada y los motivos del " +
            "reclamo",
        "La nueva Marcha Federal Universitaria ya tiene fecha y lugar confirmados. Está convocada" +
            " para el 15 de octubre y tendrá como ejes el reclamo por el financiamiento de las " +
            "universidades públicas, la recomposición salarial y la aplicación de la Ley de " +
            "Financiamiento Universitario.",
        "Redacción"));
    repositorio.registrar(new Noticia(
        "Misiones vuelve a habilitar el glifosato para fumigaciones: el agrotóxico señalado como " +
            "cancerígeno regresa a la principal zona yerbatera del país",
        "La provincia modificó la Ley de Bioinsumos y eliminó la prohibición del agrotóxico. " +
            "Mientras legisladores y el sector yerbatero argumentan que la medida brinda " +
            "previsibilidad y exige una transición progresiva con controles, organizaciones " +
            "ambientales y de salud advierten sobre las consecuencias de su regreso.",
        "Redacción"));
    repositorio.registrar(new Noticia(
        "Virginia Aparicio, científica del INTA, perseguida y censurada por investigar en " +
            "Argentina los efectos de agrotóxicos en humanos",
        "La investigadora Virginia Aparicio, responsable de detectar que en un estudio en el que el" +
            " 100% de sus participantes tiene pesticidas en sangre, es silenciada y perseguida " +
            "por el INTA",
        "Redacción"));
    repositorio.registrar(new Noticia(
        "Gracias a un escandaloso fallo de la Corte, ya no hay límites a la venta de tierras a " +
            "extranjeros",
        "Ignorando la voluntad popular, ninguneando a los excombatientes de Malvinas y " +
            "desoyendo al Congreso, la Corte restauró el artículo del DNU 70/2023 que habilita " +
            "la venta de tierras irrestricta a extranjeros",
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
