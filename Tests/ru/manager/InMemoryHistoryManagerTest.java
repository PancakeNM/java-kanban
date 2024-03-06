package ru.manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.manager.interfaces.HistoryManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    static HistoryManager manager;
    @BeforeAll
    public static void beforeAll() {
        manager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldSavePreviousVersionOfTask() {
        Epic epic = new Epic("t", "td");

        List<Task> newEpicList = new ArrayList<>();
        newEpicList.add(epic);
        manager.add(epic);
        epic.setName("tc");

        assertNotEquals(newEpicList, manager.getHistory());
    }
}