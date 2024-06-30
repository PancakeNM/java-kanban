package ru;

import ru.manager.api.utility.handlers.HttpTaskServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        try {
            httpTaskServer.start();
        } catch (IOException e) {
            System.out.println("Ошибка инициализации сервера.");
        }
    }
}
