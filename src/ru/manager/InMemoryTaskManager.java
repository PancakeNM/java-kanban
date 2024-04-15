package ru.manager;

import ru.manager.interfaces.HistoryManager;
import ru.manager.interfaces.TaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    HistoryManager historyManager;
    protected int id = 0;
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, SubTask> subTasks = new HashMap<>();

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }


    @Override
    public void addNewTask(Task task) { //метод добавления новой задачи.
        task.setId(generateNewId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void addNewSubTask(SubTask subTask) { //метод добавления новой подзадачи.
        subTask.setId(generateNewId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTaskId(subTask.getId());
        epicStatusUpdater(subTask);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        epicStatusUpdater(subTask);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void epicStatusUpdater(SubTask newSubTask) { //Метод, отвечающий за логику обновления статуса эпика
        Epic epic = epics.get(newSubTask.getEpicId()); // получение эпика, к которому относится подзадача
        List<Integer> subTaskIdsByEpicId = epic.getSubTaskIds();
        List<TaskStatus> statuses = new ArrayList<>();
        for (int id : subTaskIdsByEpicId) { //занесение статусов подзадач эпика в лист
            SubTask subTask = subTasks.get(id);
            statuses.add(subTask.getStatus());
        }
        if (!statuses.contains(TaskStatus.IN_PROGRESS) && !statuses.contains(TaskStatus.NEW)) {
            epic.setStatus(TaskStatus.DONE);
        } else if (!statuses.contains(TaskStatus.IN_PROGRESS) && !statuses.contains(TaskStatus.DONE)) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } // проверка статуса эпика и его обновление
    }

    @Override
    public void addNewEpic(Epic epic) { //метод добавления нового эпика.
        epic.setId(generateNewId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void removeAllTasks() { //удаление всех задач
        tasks.clear();
    }

    @Override
    public void removeAllEpics() { //удаление всех эпиков
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void removeAllSubTasks() { //удаление всех подзадач
        subTasks.clear();
        for (Integer id : epics.keySet()) {
            epics.get(id).removeAllSubTaskId();
        }
    }

    @Override
    public void removeTaskById(int id) { //удаление задачи по id
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(int id) { //удаление эпика по id
        for (Integer subTaskId : epics.get(id).getSubTaskIds()) {
            subTasks.remove(subTaskId);
        }
        epics.remove(id);
    }

    @Override
    public void removeSubTaskById(int id) { //удаление подзадачи по id
        SubTask subTask = subTasks.get(id);
        Epic epic = epics.get(subTask.getEpicId());
        epic.removeSubTaskId(id);
        epicStatusUpdater(subTask);
    }

    @Override
    public Task getTaskById(int id) { //получение задачи по id
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) { //получение эпика по id
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) { //метод получения
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyManager.getHistory());
    }

    @Override
    public List<SubTask> getSubTasksByEpicId(int epicId) {
        List<SubTask> subTasksByEpicId = new ArrayList<>();
        Epic epic = epics.get(epicId);
        for (Integer id : epic.getSubTaskIds()) {
            historyManager.add(subTasks.get(id));
            subTasksByEpicId.add(subTasks.get(id));
        }
        return subTasksByEpicId;
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    protected int generateNewId() { //метод генерации уникального id.
        id++;
        return id;
    }
}
