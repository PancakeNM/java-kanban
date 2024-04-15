package ru.manager;

import ru.manager.interfaces.HistoryManager;

public class FileBackedTaskManager extends InMemoryTaskManager{
    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
    }

    public void save() {

    }
}
