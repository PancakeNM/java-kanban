package ru.manager.api.utility.handlers;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.Managers;
import ru.manager.Epic;
import ru.manager.SubTask;
import ru.manager.Task;
import ru.manager.TaskStatus;
import ru.manager.api.utility.handlers.utility.SubtaskMapTypeToken;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskServerSubtaskTest {
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
    public void shouldAddSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("T", "T");
        manager.addNewEpic(epic);
        SubTask subTask = new SubTask(2, "Test 2", "Testing subtask 2",
                TaskStatus.NEW, 1);
        String subTaskJson = gson.toJson(subTask);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/subtasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest
                            .BodyPublishers
                            .ofString(subTaskJson))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse
                    .BodyHandlers
                    .ofString());
            assertEquals(201, response.statusCode());

            Map<Integer, SubTask> subTasksFromManager = manager.getSubTasks();

            assertNotNull(subTasksFromManager, "Задачи не возвращаются");
            assertEquals(1, subTasksFromManager.size(), "Некорректное колличество задач");
            assertEquals(subTask,  subTasksFromManager.get(2));
        }
    }


    @Test
    public void shouldReturnSubtaskWhenAsked() throws IOException, InterruptedException {
        Epic epic = new Epic("T", "T");
        manager.addNewEpic(epic);
        SubTask subTask = new SubTask("Test 2", "Testing subtask 2",
                TaskStatus.NEW, 1);
        manager.addNewSubTask(subTask);
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/subtasks/2");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            SubTask subtaskFromRequest = gson.fromJson(body, SubTask.class);

            assertEquals(subTask, subtaskFromRequest);
        }
    }

    @Test
    public void shouldDeleteSubtaskWhenMethodIsDELETE() throws IOException, InterruptedException {
        Epic epic = new Epic("T", "T");
        manager.addNewEpic(epic);
        SubTask subTask = new SubTask("Test 2", "Testing subtask 2",
                TaskStatus.NEW, 1);
        manager.addNewSubTask(subTask);
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/subtasks/2");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .DELETE()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(204, response.statusCode());

            assertEquals(0, manager.getSubTasks().size());
        }
    }

    @Test
    public void shouldGetAllSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("T", "T");
        manager.addNewEpic(epic);
        SubTask subTask = new SubTask("Test 2", "Testing subtask 2",
                TaskStatus.NEW, 1);
        manager.addNewSubTask(subTask);
        Map<Integer, SubTask> expected = new HashMap<>();
        expected.put(2, subTask);
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/subtasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            Map<Integer, SubTask> tasksFromRequest = gson.fromJson(body, new SubtaskMapTypeToken().getType());

            assertEquals(expected, tasksFromRequest);
        }
    }
}
