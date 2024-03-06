package ru.manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    static SubTask testSubtask;
    @BeforeAll
    public static void beforeAll() {
        testSubtask = new SubTask(1, "t", "td", 0);
    }

    @Test
    public void shouldNotBePossibleToAddSubtaskAsEpicToItself() {
        testSubtask.setEpicId(1);
        assertEquals(0, testSubtask.getEpicId());
    }

    @Test
    public void subtasksShouldEqualsWhenIdIsEqual() {
        SubTask subTask2 = new SubTask(testSubtask.getId(), "r", "r", 1);

        assertEquals(testSubtask, subTask2);
    }
}