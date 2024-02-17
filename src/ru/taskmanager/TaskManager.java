package ru.taskmanager;

import ru.taskmanager.tasktypes.Epic;
import ru.taskmanager.tasktypes.SubTask;
import ru.taskmanager.tasktypes.Task;

import java.util.HashMap;

public class TaskManager {
    private static int id = 0;
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public void addNewTask(Task task) { //метод добавления новой задачи.
        tasks.put(task.getID(), task);
    }

    public void addNewSubTask(SubTask subTask, int epicId) { //метод добавления новой подзадачи.
        subTasks.put(subTask.getID(), subTask);
        Epic epic = epics.get(epicId);
        epic.addSubTaskID(subTask.getID());
    }

    public void addNewEpic(Epic epic) { //метод добавления нового эпика.
        epics.put(epic.getID(), epic);
    }

    public void removeAllTasks() { //удаление всех задач
        tasks.clear();
    }

    public void removeAllEpics() { //удаление всех эпиков
        epics.clear();
    }

    public void removeAllSubTasks() { //удаление всех подзадач
        subTasks.clear();
    }

    public void removeTaskById(int id) { //удаление задачи по id
        tasks.remove(id);
    }

    public void removeEpicById(int id) { //удаление эпика по id
        epics.remove(id);
    }

    public void removeSubTaskById(int id) { //удаление подзадачи по id
        subTasks.remove(id);
    }

    public Task getTaskById(int id) { //получение задачи по id
        return tasks.get(id);
    }

    public Epic getEpicById(int id) { //получение эпика по id
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id){ //метод получения
        return subTasks.get(id);
    }
    public int generateNewId() { //метод генерации уникального id.
        id++;
        return id;
    }
}
