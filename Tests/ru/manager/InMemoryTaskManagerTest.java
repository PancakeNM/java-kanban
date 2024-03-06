package ru.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.manager.interfaces.TaskManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager manager;
    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager(new InMemoryHistoryManager());
    }

    @Test
    public void canFindCreatedTaskById() {
        Task task = new Task("t", "td");
        manager.addNewTask(task);

        assertEquals(task, manager.getTaskById(1));
    }

    @Test
    public void canFindCreatedEpicById() {
        Epic epic = new Epic("tt", "ttd");
        manager.addNewEpic(epic);

        assertEquals(epic, manager.getEpicById(1));
    }

    @Test
    public void canFindCreatedSubTaskById() {
        Epic epic = new Epic("te", "ted");
        manager.addNewEpic(epic);
        SubTask subTask = new SubTask("ttt", "tttd", 1);
        manager.addNewSubTask(subTask);

        assertEquals(subTask, manager.getSubTaskById(2));
    }

    @Test
    public void canFindSubTasksByEpicId() {
        Epic epic = new Epic("tt", "ttd");
        manager.addNewEpic(epic);
        SubTask subTask1 = new SubTask("ttt", "tttd", 1);
        SubTask subTask2 = new SubTask("ttt", "tttd", 1);
        SubTask subTask3 = new SubTask("ttt", "tttd", 1);
        manager.addNewSubTask(subTask1);
        manager.addNewSubTask(subTask2);
        manager.addNewSubTask(subTask3);

        List<Task> expected = new ArrayList<>();
        expected.add(subTask1);
        expected.add(subTask2);
        expected.add(subTask3);

        assertEquals(expected, manager.getSubTasksByEpicId(1));
    }

    @Test
    public void shouldOverwriteManuallyCreatedIdWithNewId() {
        Task task1 = new Task(9, "t", "td");
        int createdTaskId = task1.getId();
        manager.addNewTask(task1);
        Task task2 = manager.getTaskById(1);

        Epic epic1 = new Epic(9, "t", "td");
        int createdEpicId = epic1.getId();
        manager.addNewEpic(epic1);
        Epic epic2 = manager.getEpicById(2);

        SubTask subTask1 = new SubTask(9, "t", "td", 2);
        int createdSubtaskId = subTask1.getId();
        manager.addNewSubTask(subTask1);
        SubTask subTask2 = manager.getSubTaskById(3);

        assertNotEquals(createdTaskId, task2.getId(), "Task Id's matching, but should not");
        assertNotEquals(createdEpicId, epic2.getId(), "Epic Id's matching, but should not");
        assertNotEquals(createdSubtaskId, subTask2.getId(), "Subtask Id's matching, but should not");
    }

    @Test
    public void tasksShouldEqualsWhenAddedToManager() {
        Task task = new Task("t", "td");
        manager.addNewTask(task);

        assertEquals(task, manager.getTaskById(1));
    }

    @Test
    public void epicsShouldEqualsWhenAddedToManager() {
        Epic epic = new Epic("t", "td");
        manager.addNewEpic(epic);

        assertEquals(epic, manager.getEpicById(1));
    }

    @Test
    public void subtasksShouldEqualsWhenAddedToManager() {
        Epic epic = new Epic("te", "td");
        manager.addNewEpic(epic);
        SubTask subTask = new SubTask("t", "td", 1);
        manager.addNewSubTask(subTask);

        assertEquals(subTask, manager.getSubTaskById(2));
    }
}