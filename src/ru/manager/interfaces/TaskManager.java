package ru.manager.interfaces;

import ru.manager.Epic;
import ru.manager.SubTask;
import ru.manager.Task;

import java.util.List;

public interface TaskManager {
    void addNewTask(Task task);

    void addNewSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void epicUpdater(SubTask newSubTask);

    void addNewEpic(Epic epic);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubTasks();

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubTaskById(int id);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    List<SubTask> getSubTasksByEpicId(int epicId);

    List<Task> getHistory();
}
