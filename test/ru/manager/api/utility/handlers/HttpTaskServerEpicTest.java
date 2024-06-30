package ru.manager.api.utility.handlers;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.Managers;
import ru.manager.Epic;
import ru.manager.SubTask;
import ru.manager.TaskStatus;
import ru.manager.api.utility.handlers.utility.EpicMapTypeToken;
import ru.manager.api.utility.handlers.utility.SubtasksListTypeToken;
import ru.manager.interfaces.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskServerEpicTest {
    TaskManager manager;
    Gson gson;
    HttpTaskServer epicServer;

    @BeforeEach
    public void setUp() throws IOException {
        manager = Managers.getDefaultTaskManager();
        epicServer = new HttpTaskServer(manager);
        gson = epicServer.getGson();
        epicServer.start();
    }

    @AfterEach
    public void shutDown() {
        epicServer.stop();
    }

    @Test
    public void shouldAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Te", "ted");
        epic.setId(1);
        String epicJson = gson.toJson(epic);

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .POST(HttpRequest
                            .BodyPublishers
                            .ofString(epicJson))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse
                    .BodyHandlers
                    .ofString());
            assertEquals(201, response.statusCode());

            Map<Integer, Epic> epicsFromManager = manager.getEpics();

            assertNotNull(epicsFromManager, "Задачи не возвращаются");
            assertEquals(1, epicsFromManager.size(), "Некорректное колличество задач");
            assertEquals(epic, epicsFromManager.get(1));
        }
    }


    @Test
    public void shouldReturnEpicWhenAsked() throws IOException, InterruptedException {
        Epic epic = new Epic("Te", "ted");
        manager.addNewEpic(epic);
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics/1");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            Epic epicFromRequest = gson.fromJson(body, Epic.class);

            assertEquals(epic, epicFromRequest);
        }
    }

    @Test
    public void shouldDeleteEpicWhenMethodIsDELETE() throws IOException, InterruptedException {
        Epic epic = new Epic("Te", "ted");
        manager.addNewEpic(epic);
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics/1");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .DELETE()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(204, response.statusCode());

            assertEquals(0, manager.getEpics().size());
        }
    }

    @Test
    public void shouldGetAllEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("Te", "ted");
        manager.addNewEpic(epic);
        Map<Integer, Epic> expected = new HashMap<>();
        expected.put(1, epic);
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            Map<Integer, Epic> epicsFromRequest = gson.fromJson(body, new EpicMapTypeToken().getType());

            assertEquals(expected, epicsFromRequest);
        }
    }

    @Test
    public void shouldGetAllSubtasksByEpicId() throws IOException, InterruptedException {
        Epic epic = new Epic("T", "T");
        manager.addNewEpic(epic);
        SubTask subTask = new SubTask("Test 2", "Testing subtask 2",
                TaskStatus.NEW, 1);
        manager.addNewSubTask(subTask);
        SubTask subTask1 = new SubTask("Test 1", "Testing subtask 1",
                TaskStatus.NEW,  1);
        subTask1.setStartTime(subTask.getStartTime().plusDays(1));
        manager.addNewSubTask(subTask1);
        List<SubTask> expected = new ArrayList<>();
        expected.add(subTask);
        expected.add(subTask1);
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics/1/subtasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            List<SubTask> subTasksByEpic = gson.fromJson(body, new SubtasksListTypeToken().getType());

            assertEquals(expected, subTasksByEpic);
        }
    }
}
