package ru.manager.api.utility.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.manager.SubTask;
import ru.manager.api.utility.handlers.utility.ErrorHandler;
import ru.manager.interfaces.TaskManager;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    public SubtaskHandler(TaskManager manager, Gson gson, ErrorHandler errorHandler) {
        super(manager, gson, errorHandler);
    }

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
                            sendText(h, response, 200);
                        } else {
                            int id = Integer.parseInt(splitPath[2]);
                            response = gson.toJson(manager.getSubTaskById(id));
                            sendText(h, response, 200);
                        }
                    }
                    case "POST" -> {
                        InputStream inputStream = h.getRequestBody();
                        String subTaskString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        SubTask subTask = gson.fromJson(subTaskString, SubTask.class);
                        if (manager.getSubTaskById(subTask.getId()) != null) {
                            manager.updateSubTask(subTask);
                            sendText(h, "Подзадача обновлена.", 201);
                        } else {
                            manager.addNewSubTask(subTask);
                            sendText(h, "Подзадача добавлена.", 201);
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
