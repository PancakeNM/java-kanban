package ru.manager.api.utility.handlers.utility;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import ru.manager.api.ErrorType;
import ru.manager.utility.ManagerIOException;
import ru.manager.utility.NotFoundException;
import ru.manager.utility.ValidationException;

import java.nio.charset.StandardCharsets;


public class ErrorHandler {
    final Gson gson;

    public ErrorHandler(Gson gson) {
        this.gson = gson;
    }

    public void handle(HttpExchange h, Exception e) {
        try {
            if (e instanceof ManagerIOException) {
                sendText(h, ErrorType.BAD_GATEWAY, gson.toJson(e.getMessage()));
                return;
            }
            if (e instanceof NotFoundException) {
                sendText(h, ErrorType.NOT_FOUND, gson.toJson(e.getMessage()));
                return;
            }
            if (e instanceof ValidationException) {
                sendText(h, ErrorType.CONFLICT, gson.toJson(e.getMessage()));
                return;
            }
            e.printStackTrace();
            sendText(h, ErrorType.INTERNAL_ERROR, gson.toJson(e.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendText(HttpExchange h, ErrorType type, String json) {
        try (h) {
            try {
                byte[] response = json.getBytes(StandardCharsets.UTF_8);
                h.getResponseHeaders().add("Content-type", "application/json;string/StandardCharsets.UTF_8");
                switch (type) {
                    case NOT_FOUND -> h.sendResponseHeaders(404, 0);
                    case BAD_GATEWAY -> h.sendResponseHeaders(502, 0);
                    case INTERNAL_ERROR -> h.sendResponseHeaders(500, 0);
                    case CONFLICT -> h.sendResponseHeaders(409, 0);
                }
                h.getResponseBody().write(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
