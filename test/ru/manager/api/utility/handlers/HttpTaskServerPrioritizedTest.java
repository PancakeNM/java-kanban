package ru.manager.api.utility.handlers;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.Managers;
import ru.manager.Epic;
import ru.manager.Task;
import ru.manager.api.utility.handlers.utility.PrioritizedSetTypeToken;
import ru.manager.interfaces.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerPrioritizedTest {
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
    public void shouldReturnPrioritizedListWhenAsked() throws IOException, InterruptedException {
        Task task = new Task("test 1", "Test task 1");
        task.setStartTime(task.getStartTime().withNano(0));
        Task task1 = new Task("test 2", "Test task 2");
        task1.setStartTime(task.getEndTime().minusDays(1).withNano(0));
        Task task2 = new Task("test 3", "Test task 3");
        task2.setStartTime(task.getEndTime().minusDays(2).withNano(0));
        manager.addNewTask(task);
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        Set<Task> expected = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        expected.add(task);
        expected.add(task1);
        expected.add(task2);
        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/prioritized");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            Set<Task> prioritizedList = gson.fromJson(body, new PrioritizedSetTypeToken().getType());

            assertEquals(expected, prioritizedList);
        }
    }
}
