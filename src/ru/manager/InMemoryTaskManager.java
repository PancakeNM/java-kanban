package ru.manager;

import ru.manager.interfaces.HistoryManager;
import ru.manager.interfaces.TaskManager;
import ru.manager.utility.NotFoundException;
import ru.manager.utility.ValidationException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    HistoryManager historyManager;
    private int id = 0;
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, SubTask> subTasks = new HashMap<>();
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }


    @Override
    public void addNewTask(Task task) { //метод добавления новой задачи.
        task.setId(generateNewId());
        validateTaskTime(task);
        prioritizedTasks.add(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addNewSubTask(SubTask subTask) { //метод добавления новой подзадачи.
        subTask.setId(generateNewId());
        validateTaskTime(subTask);
        prioritizedTasks.add(subTask);
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTaskId(subTask.getId());
        epicUpdater(subTask);
    }

    private boolean isCrossing(Task t, Task task) {
        return t.getStartTime().isBefore(task.getEndTime()) && t.getEndTime().isAfter(task.getStartTime())
                || task.getStartTime().isBefore(t.getEndTime()) && task.getEndTime().isAfter(t.getStartTime())
                || t.getStartTime().isAfter(task.getStartTime()) && t.getEndTime().isBefore(task.getEndTime())
                || t.getStartTime().isBefore(task.getStartTime()) && t.getEndTime().isAfter(task.getEndTime());
    }

    public void validateTaskTime(Task task) {
        for (Task t : getPrioritizedTasks()) {
            if (t.getId() == task.getId()) {
                continue;
            }
            if (isCrossing(t, task)) {
                throw new ValidationException("Пересечение с задачей: " + t.getId());
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        Task original = tasks.get(task.getId());
        if (original == null) {
            throw new NotFoundException("Task id=" + task.getId());
        }

        validateTaskTime(task);
        prioritizedTasks.remove(original);
        prioritizedTasks.add(task);
        tasks.put(task.getId(), task);

    }

    @Override
    public void updateSubTask(SubTask subTask) {
        SubTask original = subTasks.get(subTask.getId());
        if (original == null) {
            throw new NotFoundException("SubTask id=" + subTask.getId());
        }

        validateTaskTime(subTask);
        prioritizedTasks.remove(original);
        prioritizedTasks.add(subTask);
        epicUpdater(subTask);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void epicUpdater(SubTask newSubTask) { //Метод, отвечающий за логику обновления полей эпика
        Epic epic = epics.get(newSubTask.getEpicId()); // получение эпика, к которому относится подзадача
        List<Integer> subTaskIdsByEpicId = epic.getSubTaskIds();
        List<TaskStatus> statuses = new ArrayList<>();
        LocalDateTime bufferedStartTime = newSubTask.getStartTime();
        LocalDateTime bufferedEndTime = newSubTask.getEndTime();
        Duration epicDuration = Duration.ofMinutes(0);
        for (int id : subTaskIdsByEpicId) { //занесение статусов подзадач эпика в лист и расчет временнЫх полей
            SubTask subTask = subTasks.get(id);
            statuses.add(subTask.getStatus());
            epicDuration = epicDuration.plus(subTask.getDuration());
            if (bufferedStartTime.isAfter(subTask.getStartTime())) {
                bufferedStartTime = subTask.getStartTime();
            }
            if (bufferedEndTime.isBefore(subTask.getEndTime())) {
                bufferedEndTime = subTask.getEndTime();
            }
        }
        epic.setDuration(epicDuration);
        epic.setStartTime(bufferedStartTime);
        epic.setEndTime(bufferedEndTime);
        if (!statuses.contains(TaskStatus.IN_PROGRESS) && !statuses.contains(TaskStatus.NEW)) {
            epic.setStatus(TaskStatus.DONE);
        } else if (!statuses.contains(TaskStatus.IN_PROGRESS) && !statuses.contains(TaskStatus.DONE)) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } // проверка статуса эпика и его обновление
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
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
    public void removeTaskById(int id) throws NotFoundException { //удаление задачи по id
        if (tasks.get(id) != null) {
            tasks.remove(id);
        } else {
            throw new NotFoundException("Задача с id " + id + " не найдена.");
        }
    }

    @Override
    public void removeEpicById(int id) throws NotFoundException { //удаление эпика по id
        try {
            for (Integer subTaskId : epics.get(id).getSubTaskIds()) {
                subTasks.remove(subTaskId);
            }
            epics.remove(id);
        } catch (NullPointerException e) {
            throw new NotFoundException("Эпик с id " + id + " не найден.");
        }
    }

    @Override
    public void removeSubTaskById(int id) throws NotFoundException { //удаление подзадачи по id
        try {
            SubTask subTask = subTasks.get(id);
            Epic epic = epics.get(subTask.getEpicId());
            epic.removeSubTaskId(id);
            epicUpdater(subTask);
            subTasks.remove(id);
        } catch (NullPointerException e) {
            throw new NotFoundException("Подзадача с id " + id + " не найдена.");
        }
    }

    @Override
    public Task getTaskById(int id) throws NotFoundException { //получение задачи по id
        try {
            if (tasks.containsKey(id)) {
                historyManager.add(tasks.get(id));
            }
            return tasks.get(id);
        } catch (NullPointerException e) {
            throw new NotFoundException("Задача с id " + id + " не найдена.");
        }
    }

    @Override
    public Epic getEpicById(int id) throws NotFoundException { //получение эпика по id
        try {
            if (epics.containsKey(id)) {
                historyManager.add(epics.get(id));
            }
            return epics.get(id);
        } catch (NullPointerException e) {
            throw new NotFoundException("Эпик с id " + id + " не найден.");
        }
    }

    @Override
    public SubTask getSubTaskById(int id) { //метод получения
        try {
            if (subTasks.containsKey(id)) {
                historyManager.add(subTasks.get(id));
            }
            return subTasks.get(id);
        } catch (NullPointerException e) {
            throw new NotFoundException("Подзадача с id " + id + " не найдена.");
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyManager.getHistory());
    }

    @Override
    public List<SubTask> getSubTasksByEpicId(int epicId) {
        try {
            List<SubTask> subTasksByEpicId = new ArrayList<>();
            Epic epic = epics.get(epicId);
            for (Integer id : epic.getSubTaskIds()) {
                historyManager.add(subTasks.get(id));
                subTasksByEpicId.add(subTasks.get(id));
            }
            return subTasksByEpicId;
        } catch (NullPointerException e) {
            throw new NotFoundException("Подзадачи эпика с id " + epicId + " не найдены");
        }
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

    private int generateNewId() { //метод генерации уникального id.
        id++;
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
