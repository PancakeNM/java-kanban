package ru.manager.api.utility.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange h) {
        try (h) {
            try {
                String response = gson.toJson(manager.getPrioritizedTasks());
                sendText(h, response, 200);
            } catch (Exception e) {
                errorHandler.handle(h, e);
            }
        }
    }
}
