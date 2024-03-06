package ru.manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    static Task task1;
    @BeforeAll
    public static void beforeAll() {
        task1 = new Task(0, "t", "t");
    }

    @Test
    public void tasksShouldEqualsWhenIdIsEqual() {
        Task task2 = new Task(task1.getId(), "r", "r");

        assertEquals(task1, task2);
    }

    @Test
    public void shouldReturnName() {
        assertEquals("t", task1.getName());
    }

    @Test
    public void shouldReturnDescription() {
        assertEquals("t", task1.getDescription());
    }

    @Test
    public void subtasksShouldEqualsWhenIdIsEqual() {
        SubTask subTask1 = new SubTask(0, "t", "t", 0);
        SubTask subTask2 = new SubTask(subTask1.getId(), "r", "r", 1);

        assertEquals(subTask1, subTask2);
    }
}