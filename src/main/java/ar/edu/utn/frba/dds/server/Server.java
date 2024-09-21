package ar.edu.utn.frba.dds.server;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;

public class Server {
    public void start() {
        Bootstrap.init();
        var app = Javalin.create(config -> {
            initializeStaticFiles(config);
            initializeTemplating(config);
        });

        new Router().configure(app);
        app.start(9001);
    }

    private void initializeTemplating(JavalinConfig config) {
        config.fileRenderer(
            new JavalinRenderer().register("hbs", new JavalinHandlebars())
        );
    }

    private static void initializeStaticFiles(JavalinConfig config) {
        config.staticFiles.add(staticFileConfig -> {
            staticFileConfig.hostedPath = "/assets";
            staticFileConfig.directory = "/assets";
        });
    }
}