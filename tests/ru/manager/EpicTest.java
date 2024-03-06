package ru.manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    static Epic epic1;
    @BeforeAll
    public static void beforeAll() {
        epic1 = new Epic(0, "t", "t");
    }

    @Test
    public void epicsShouldEqualsWhenIdIsEqual() {
        Epic epic2 = new Epic(epic1.getId(), "r", "r");

        assertEquals(epic1, epic2);
    }

    @Test
    public void shouldNotBePossibleToAddEpicAsSubtaskToItself() {
        epic1.addSubTaskId(epic1.getId());
        List<Integer> expected = new ArrayList<>();

        assertEquals(expected, epic1.getSubTaskIds());
    }
}