package ru.manager.api.utility.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
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
