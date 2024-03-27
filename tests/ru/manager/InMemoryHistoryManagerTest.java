package ru.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.manager.interfaces.HistoryManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager manager;
    @BeforeEach
    public void beforeAll() {
        manager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldSaveOnlyLastVersionOfTask() {
        Task epic = new Epic("t", "td");

        List<Task> newEpicList = new ArrayList<>();
        newEpicList.add(epic);
        manager.add(epic);
        epic.setName("tc");

        assertEquals(newEpicList, manager.getHistory());
    }

    @Test
    public void shouldRemoveTaskFromList() {
        Task task = new Task(0, "t", "td");
        Task task2 = new Task(1, "tt", "ttd");
        Task task3 = new Task(9, "ttt", "tttd");

        List<Task> newTaskList = new ArrayList<>();
        newTaskList.add(task);
        newTaskList.add(task3);
        manager.add(task);
        manager.add(task2);
        manager.add(task3);
        manager.remove(task2.id);

        assertEquals(newTaskList, manager.getHistory());
    }
}