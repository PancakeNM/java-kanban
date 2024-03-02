package ru;

import ru.manager.InMemoryHistoryManager;
import ru.manager.InMemoryTaskManager;
import ru.manager.interfaces.HistoryManager;
import ru.manager.interfaces.TaskManager;

public class Managers {

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager(getDefaultHistoryManager());
    }

    public static HistoryManager getDefaultHistoryManager(){
        return new InMemoryHistoryManager();
    }
}
