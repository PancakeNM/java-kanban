package ru.manager.api.utility.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.Managers;
import ru.manager.SubTask;
import ru.manager.Task;
import ru.manager.api.utility.adapters.DurationTypeAdapter;
import ru.manager.api.utility.adapters.LocalDateTimeAdapter;
import ru.manager.interfaces.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    public static HttpServer httpServer;
    public static final int PORT = 8080;
    public static Gson gson;
    public static TaskManager manager = Managers.getDefaultTaskManager();
    public static ErrorHandler errorHandler = new ErrorHandler(getGson());
    public static BaseHttpHandler handler = new BaseHttpHandler();


    public static HttpServer getHttpServer() {
        return httpServer;
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
                    String method = h.getRequestMethod();
                    String response;
                    switch (method) {
                        case "GET" -> {
                            String[] splitPath = path.split("/");
                            if (splitPath.length < 3) {
                                response = gson.toJson(manager.getTasks());
                                handler.sendText(h, response, 200);
                            } else {
                                int id = Integer.parseInt(splitPath[2]);
                                response = gson.toJson(manager.getTaskById(id));
                                handler.sendText(h, response, 200);
                            }
                        }
                        case "POST" -> {
                            InputStream inputStream = h.getRequestBody();
                            String taskString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                            Task task = gson.fromJson(taskString, Task.class);
                            if (manager.getTaskById(task.getId()) != null) {
                                manager.updateTask(task);
                                handler.sendText(h, "Задача обновлена.", 201);
                            } else {
                                manager.addNewTask(task);
                                handler.sendText(h, "Задача добавлена.", 201);
                            }
                        }
                        case "DELETE" -> {
                            String[] splitPath = path.split("/");
                            int id = Integer.parseInt(splitPath[2]);
                            manager.removeTaskById(id);
                            h.sendResponseHeaders(204, -1);
                        }
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
                    String path = h.getRequestURI().getPath();
                    String method = h.getRequestMethod();
                    String response;
                    switch (method) {
                        case "GET" -> {
                            String[] splitPath = path.split("/");
                            if (splitPath.length < 3) {
                                response = gson.toJson(manager.getSubTasks());
                                handler.sendText(h, response, 200);
                            } else {
                                int id = Integer.parseInt(splitPath[2]);
                                response = gson.toJson(manager.getSubTaskById(id));
                                handler.sendText(h, response, 200);
                            }
                        }
                        case "POST" -> {
                            InputStream inputStream = h.getRequestBody();
                            String SubTaskString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                            SubTask subTask = gson.fromJson(SubTaskString, SubTask.class);
                            if (manager.getSubTaskById(subTask.getId()) != null) {
                                manager.updateSubTask(subTask);
                                handler.sendText(h, "Подзадача обновлена.", 201);
                            } else {
                                manager.addNewSubTask(subTask);
                                handler.sendText(h, "Подзадача добавлена.", 201);
                            }
                        }
                        case "DELETE" -> {
                            String[] splitPath = path.split("/");
                            int id = Integer.parseInt(splitPath[2]);
                            manager.removeSubTaskById(id);
                            h.sendResponseHeaders(204, -1);
                        }
                    }
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
                    String path = h.getRequestURI().getPath();
                    String method = h.getRequestMethod();
                    String response;
                    switch (method) {
                        case "GET" -> {
                            String[] splitPath = path.split("/");
                            if (splitPath.length < 3) {
                                response = gson.toJson(manager.getEpics());
                                handler.sendText(h, response, 200);
                            } else if (path.contains("subtasks")) {
                                int id = Integer.parseInt(splitPath[2]);
                                response = gson.toJson(manager.getSubTasksByEpicId(id));
                                handler.sendText(h, response, 200);
                            } else {
                                int id = Integer.parseInt(splitPath[2]);
                                response = gson.toJson(manager.getEpicById(id));
                                handler.sendText(h, response, 200);
                            }
                        }
                        case "POST" -> {
                            String[] splitPath = path.split("/");
                            int id = Integer.parseInt(splitPath[2]);
                            manager.removeEpicById(id);
                            h.sendResponseHeaders(204, -1);
                        }
                    }
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
                    String response = gson.toJson(manager.getPrioritizedTasks());
                    handler.sendText(h, response, 200);
                } catch (Exception e) {
                    errorHandler.handle(h, e);
                }
            }
        }
    }

    public static Gson getGson() {
        return gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();
    }
}
