package ru.manager.api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.Managers;
import ru.manager.api.handler.ErrorHandler;
import ru.manager.interfaces.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static HttpServer httpServer;
    public static final int PORT = 8080;
    public static Gson gson;

    public static TaskManager manager;

    public HttpTaskServer() {
        gson = new Gson();
        manager = Managers.getDefaultTaskManager();
    }

    public static HttpServer getHttpServer() {
        return httpServer;
    }

    public static Gson getGson() {
        return gson;
    }

    public static TaskManager getManager() {
        return manager;
    }

    public void stop() {
        httpServer.stop(1);
    }

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
            try (httpExchange) {
                try {

                } catch (Exception e) {

                }
            }
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
