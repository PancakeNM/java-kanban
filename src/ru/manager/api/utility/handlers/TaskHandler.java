package ru.manager.api.utility.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.manager.Task;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
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
                            sendText(h, response, 200);
                        } else {
                            int id = Integer.parseInt(splitPath[2]);
                            response = gson.toJson(manager.getTaskById(id));
                            sendText(h, response, 200);
                        }
                    }
                    case "POST" -> {
                        InputStream inputStream = h.getRequestBody();
                        String taskString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        Task task = gson.fromJson(taskString, Task.class);
                        if (manager.getTaskById(task.getId()) != null) {
                            manager.updateTask(task);
                            sendText(h, "Задача обновлена.", 201);
                        } else {
                            manager.addNewTask(task);
                            sendText(h, "Задача добавлена.", 201);
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
