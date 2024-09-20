package ar.edu.utn.frba.dds.server;

import io.javalin.Javalin;

public class Server {
    public static void main(String[] args) {
        var app = Javalin.create(/*config*/)
                .get("/", ctx -> ctx.result("Hello World"))
                .start(9001);
    }
}