package ru.manager.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static HttpServer httpServer;
    public static final int PORT = 8080;

    public static void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.createContext("/subtasks", new SubtaskHandler());
        httpServer.createContext("/epics", new EpicHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
        httpServer.start();
        System.out.printf("Сервер запущен на %s порту.", PORT);
    }

    public static void main(String[] args) {
        try {
            start();
        } catch (IOException e) {
            System.out.println("Ошибка инициализации сервера.");
        }
    }

    static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

        }
    }

    static class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

        }
    }

    static class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

        }
    }

    static class PrioritizedHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

        }
    }
}
