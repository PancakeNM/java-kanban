package ru.manager;

import ru.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static int id = 0;
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public void addNewTask(Task task) { //метод добавления новой задачи.
        tasks.put(task.getId(), task);
    }

    public void addNewSubTask(SubTask subTask) { //метод добавления новой подзадачи.
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTaskId(subTask.getId());
        epicStatusUpdater(subTask);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }
    
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        epicStatusUpdater(subTask);
    }
    
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    private void epicStatusUpdater(SubTask newSubTask){ //Метод, отвечающий за логику обновления статуса эпика
        Epic epic = epics.get(newSubTask.getEpicId()); // получение эпика, к которому относится подзадача
        ArrayList<Integer> subTaskIdsByEpicId = epic.getSubTaskIds();
        ArrayList<TaskStatus> statuses = new ArrayList<>();
        for(int id : subTaskIdsByEpicId){ //занесение статусов подзадач эпика в лист
            SubTask subTask = subTasks.get(id);
            statuses.add(subTask.getStatus());
        }
        if (!statuses.contains(TaskStatus.IN_PROGRESS) && !statuses.contains(TaskStatus.NEW)){
            epic.setStatus(TaskStatus.DONE);
        } else if (!statuses.contains(TaskStatus.IN_PROGRESS) && !statuses.contains(TaskStatus.DONE)) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } // проверка статуса эпика и его обновление
    }

    public void addNewEpic(Epic epic) { //метод добавления нового эпика.
        epics.put(epic.getId(), epic);
    }

    public void removeAllTasks() { //удаление всех задач
        tasks.clear();
    }

    public void removeAllEpics() { //удаление всех эпиков
        epics.clear();
        subTasks.clear();
    }

    public void removeAllSubTasks() { //удаление всех подзадач
        subTasks.clear();
        for(Integer id : epics.keySet()){
            epics.get(id).removeAllSubTaskId();
        }
    }

    public void removeTaskById(int id) { //удаление задачи по id
        tasks.remove(id);
    }

    public void removeEpicById(int id) { //удаление эпика по id
        for(Integer subTaskId : epics.get(id).getSubTaskIds()){
            subTasks.remove(subTaskId);
        }
        epics.remove(id);
    }

    public void removeSubTaskById(int id) { //удаление подзадачи по id
        SubTask subTask = subTasks.get(id);
        Epic epic = epics.get(subTask.getEpicId());
        epic.removeSubTaskId(id);
        epicStatusUpdater(subTask);
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

    public ArrayList<SubTask> getSubTasksByEpicId(int epicId) {
        ArrayList<SubTask> subTasksByEpicId = new ArrayList<>();
        Epic epic = epics.get(epicId);
        for (Integer id : epic.getSubTaskIds()){
            subTasksByEpicId.add(subTasks.get(id));
        }
        return subTasksByEpicId;
    }
    public int generateNewId() { //метод генерации уникального id.
        id++;
        return id;
    }
}
