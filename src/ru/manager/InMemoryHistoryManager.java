package ru.manager;

import ru.manager.interfaces.HistoryManager;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    List<Task> history = new ArrayList<>();

    @Override
    public void add (Task task) {
        sizeChecker();
        history.add(new Task(task));
    }

    @Override
    public List<Task> getHistory(){
        return new ArrayList<>(history);
    }

    public void sizeChecker() {
        if (history.size() == 10){
            history.remove(0);
        }
    }
}
