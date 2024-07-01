package ru.manager.api.utility.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.manager.api.utility.handlers.utility.ErrorHandler;
import ru.manager.interfaces.TaskManager;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    public PrioritizedHandler(TaskManager manager, Gson gson, ErrorHandler errorHandler) {
        super(manager, gson, errorHandler);
    }

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
