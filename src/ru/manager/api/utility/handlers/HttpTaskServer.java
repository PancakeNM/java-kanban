package ru.manager.api.utility.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import ru.Managers;
import ru.manager.api.utility.adapters.DurationTypeAdapter;
import ru.manager.api.utility.adapters.LocalDateTimeAdapter;
import ru.manager.api.utility.handlers.utility.ErrorHandler;
import ru.manager.interfaces.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    public HttpServer httpServer;
    public final int port = 8080;
    public Gson gson;
    public TaskManager manager = Managers.getDefaultTaskManager();
    public ErrorHandler errorHandler = new ErrorHandler(getGson());

    public HttpTaskServer() {
    }

    public HttpTaskServer(TaskManager manager) {
        this.manager = manager;
    }

    public TaskManager getManager() {
        return manager;
    }

    public void stop() {
        httpServer.stop(1);
    }

    public void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);

        httpServer.createContext("/tasks", new TaskHandler(manager, gson, errorHandler));
        httpServer.createContext("/subtasks", new SubtaskHandler(manager, gson, errorHandler));
        httpServer.createContext("/epics", new EpicHandler(manager, gson, errorHandler));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager, gson, errorHandler));
        httpServer.start();
        System.out.printf("Сервер запущен на %s порту.", port);
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public Gson getGson() {
        return gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();
    }
}
