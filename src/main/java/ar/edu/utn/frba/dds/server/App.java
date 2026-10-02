package ar.edu.utn.frba.dds.server;

public class App {
    public static void main(String[] args) {
        Bootstrap.init();
        new Server().start();
    }
}
