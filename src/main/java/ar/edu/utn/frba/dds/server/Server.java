package ar.edu.utn.frba.dds.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public class Server {
    public void start() {
        Bootstrap.init();
        var app = Javalin.create(config -> {
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/assets";
                staticFileConfig.directory = "/assets";
            });
        });
        new Router().configure(app);
        app.start(9001);
    }
}