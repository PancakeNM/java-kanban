package ru.manager.api.utility.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.manager.api.utility.handlers.utility.ErrorHandler;
import ru.manager.interfaces.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    TaskManager manager;
    Gson gson;
    ErrorHandler errorHandler;

    public BaseHttpHandler(TaskManager manager, Gson gson, ErrorHandler errorHandler) {
        this.manager = manager;
        this.gson = gson;
        this.errorHandler = errorHandler;
    }
    protected void sendText(HttpExchange h, String text, int code) throws IOException {
        try (h) {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(code, resp.length);
            h.getResponseBody().write(resp);
        }
    }
}
