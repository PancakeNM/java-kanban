package ru;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    public void shouldReturnInitializedTaskManager() {
        assertNotNull(Managers.getDefaultTaskManager());
    }

    @Test
    public void shouldReturnInitializedHistoryManager() {
        assertNotNull(Managers.getDefaultHistoryManager());
    }
}