package ar.edu.utn.frba.dds.server;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        Bootstrap.init();
        new GrpcServer().start();
        new Server().start();
    }
}
