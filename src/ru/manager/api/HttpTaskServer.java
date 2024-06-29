package ru.manager.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    public static ErrorHandler errorHandler;

    public HttpTaskServer() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();;
        manager = Managers.getDefaultTaskManager();
        errorHandler = new ErrorHandler(gson);
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
        public void handle(HttpExchange h) {
            try (h) {
                try {
                    String path = h.getRequestURI().getPath();
                    String[] splitPath = path.split("/");
                    if (splitPath.length < 3) {
                        String response = gson.toJson(manager.getTasks());
                    }
                } catch (Exception e) {
                    errorHandler.handle(h, e);
                }
            }
        }
    }

    static class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange h) {
            try (h) {
                try {

                } catch (Exception e) {
                    errorHandler.handle(h, e);
                }
            }
        }
    }

    static class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange h) {
            try (h) {
                try {

                } catch (Exception e) {
                    errorHandler.handle(h, e);
                }
            }
        }
    }

    static class PrioritizedHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange h) {
            try (h) {
                try {

                } catch (Exception e) {
                    errorHandler.handle(h, e);
                }
            }
        }
    }
}
