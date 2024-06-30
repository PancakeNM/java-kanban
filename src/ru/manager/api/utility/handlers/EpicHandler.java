package ru.manager.api.utility.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.manager.Epic;
import ru.manager.api.utility.handlers.utility.ErrorHandler;
import ru.manager.interfaces.TaskManager;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    public EpicHandler(TaskManager manager, Gson gson, ErrorHandler errorHandler) {
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
                            response = gson.toJson(manager.getEpics());
                            sendText(h, response, 200);
                        } else if (path.contains("subtasks")) {
                            int id = Integer.parseInt(splitPath[2]);
                            response = gson.toJson(manager.getSubTasksByEpicId(id));
                            sendText(h, response, 200);
                        } else {
                            int id = Integer.parseInt(splitPath[2]);
                            response = gson.toJson(manager.getEpicById(id));
                            sendText(h, response, 200);
                        }
                    }
                    case "POST" -> {
                        InputStream inputStream = h.getRequestBody();
                        String epicString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        Epic epic = gson.fromJson(epicString, Epic.class);
                        if (manager.getEpicById(epic.getId()) != null) {
                            manager.updateEpic(epic);
                            sendText(h, "Задача обновлена.", 201);
                        } else {
                            manager.addNewEpic(epic);
                            sendText(h, "Задача добавлена.", 201);
                        }
                    }
                    case "DELETE" -> {
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
