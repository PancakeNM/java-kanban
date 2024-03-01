package ru.manager;

import ru.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private static int id = 0;
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public List<Integer> taskIdHistory = new ArrayList<>();


    @Override
    public void addNewTask (Task task) { //метод добавления новой задачи.
        task.setId(generateNewId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void addNewSubTask (SubTask subTask) { //метод добавления новой подзадачи.
        subTask.setId(generateNewId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTaskId(subTask.getId());
        epicStatusUpdater(subTask);
    }


    @Override
    public void updateTask (Task task) {
        tasks.put(task.getId(), task);
    }
    
    @Override
    public void updateSubTask (SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        epicStatusUpdater(subTask);
    }
    
    @Override
    public void updateEpic (Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void epicStatusUpdater (SubTask newSubTask){ //Метод, отвечающий за логику обновления статуса эпика
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

    @Override
    public void addNewEpic (Epic epic) { //метод добавления нового эпика.
        epic.setId(generateNewId());
        epics.put(epic.getId(), epic);
    }


    @Override
    public void removeAllTasks () { //удаление всех задач
        tasks.clear();
    }

    @Override
    public void removeAllEpics () { //удаление всех эпиков
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void removeAllSubTasks () { //удаление всех подзадач
        subTasks.clear();
        for(Integer id : epics.keySet()){
            epics.get(id).removeAllSubTaskId();
        }
    }


    @Override
    public void removeTaskById (int id) { //удаление задачи по id
        tasks.remove(id);
    }

    @Override
    public void removeEpicById (int id) { //удаление эпика по id
        for(Integer subTaskId : epics.get(id).getSubTaskIds()){
            subTasks.remove(subTaskId);
        }
        epics.remove(id);
    }

    @Override
    public void removeSubTaskById (int id) { //удаление подзадачи по id
        SubTask subTask = subTasks.get(id);
        Epic epic = epics.get(subTask.getEpicId());
        epic.removeSubTaskId(id);
        epicStatusUpdater(subTask);
    }


    @Override
    public Task getTaskById (int id) { //получение задачи по id
        listSizeChecker();
        taskIdHistory.add(id);
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById (int id) { //получение эпика по id
        listSizeChecker();
        taskIdHistory.add(id);
        return epics.get(id);
    }

    @Override
    public SubTask getSubTaskById (int id){ //метод получения
        listSizeChecker();
        taskIdHistory.add(id);
        return subTasks.get(id);
    }

    @Override
    public List<Task> getHistory(){
        List<Task> history = new ArrayList<>();
        for (Integer id : taskIdHistory){
            if(tasks.containsKey(id)){
                history.add(tasks.get(id));
            } else if(epics.containsKey(id)) {
                history.add(epics.get(id));
            } else if(subTasks.containsKey(id)) {
                history.add(subTasks.get(id));
            }
        }
        return history;
    }

    private void listSizeChecker(){
        if (taskIdHistory.size() == 10){
            tasks.remove(0);
        }
    }

    @Override
    public ArrayList<SubTask> getSubTasksByEpicId (int epicId) {
        ArrayList<SubTask> subTasksByEpicId = new ArrayList<>();
        Epic epic = epics.get(epicId);
        for (Integer id : epic.getSubTaskIds()){
            subTasksByEpicId.add(subTasks.get(id));
        }
        return subTasksByEpicId;
    }
    private int generateNewId() { //метод генерации уникального id.
        id++;
        return id;
    }
}
