package ru.manager.api.utility.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.manager.api.utility.handlers.utility.ErrorHandler;
import ru.manager.interfaces.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    HttpTaskServer httpTaskServer = new HttpTaskServer();
    TaskManager manager = httpTaskServer.getManager();
    Gson gson = httpTaskServer.getGson();
    ErrorHandler errorHandler = httpTaskServer.getErrorHandler();
    protected void sendText(HttpExchange h, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, 0);
        h.getResponseBody().write(resp);
        h.close();
    }
}
