package ru.manager.api.utility.handlers;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.Managers;
import ru.manager.Task;
import ru.manager.TaskStatus;
import ru.manager.api.utility.handlers.utility.TaskMapTypeToken;
import ru.manager.interfaces.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTaskTest {
    TaskManager manager;
    Gson gson;
    HttpTaskServer taskServer;

    @BeforeEach
    public void setUp() throws IOException {
        manager = Managers.getDefaultTaskManager();
        taskServer = new HttpTaskServer(manager);
        gson = taskServer.getGson();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void shouldAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        task.setId(1);
        String taskJson = gson.toJson(task);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest
                            .BodyPublishers
                            .ofString(taskJson))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse
                    .BodyHandlers
                    .ofString());
            assertEquals(201, response.statusCode());

            Map<Integer, Task> tasksFromManager = manager.getTasks();

            assertNotNull(tasksFromManager, "Задачи не возвращаются");
            assertEquals(1, tasksFromManager.size(), "Некорректное колличество задач");
            assertEquals(task, tasksFromManager.get(1));
        }
    }


    @Test
    public void shouldReturnTaskWhenAsked() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        manager.addNewTask(task);
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/tasks/1");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            Task taskFromRequest = gson.fromJson(body, Task.class);

            assertEquals(task, taskFromRequest);
        }
    }

    @Test
    public void shouldDeleteTaskWhenMethodIsDELETE() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        manager.addNewTask(task);
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/tasks/1");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .DELETE()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(204, response.statusCode());

            assertEquals(0, manager.getTasks().size());
        }
    }

    @Test
    public void shouldGetAllTasks() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        manager.addNewTask(task);
        Map<Integer, Task> expected = new HashMap<>();
        expected.put(1, task);
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            Map<Integer, Task> tasksFromRequest = gson.fromJson(body, new TaskMapTypeToken().getType());

            assertEquals(expected, tasksFromRequest);
        }
    }
}