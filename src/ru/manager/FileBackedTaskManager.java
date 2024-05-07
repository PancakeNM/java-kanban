package ru.manager;

import ru.manager.interfaces.HistoryManager;

public class FileBackedTaskManager extends InMemoryTaskManager{
    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
    }

    public void save() {

    }

    @Override
    public void addNewTask(Task task) { //метод добавления новой задачи.
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewSubTask(SubTask subTask) { //метод добавления новой подзадачи.
        super.addNewSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void addNewEpic(Epic epic) { //метод добавления нового эпика.
        super.addNewEpic(epic);
        save();
    }

    @Override
    public void removeAllTasks() { //удаление всех задач
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() { //удаление всех эпиков
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubTasks() { //удаление всех подзадач
        super.removeAllSubTasks();
        save();
    }

    @Override
    public void removeTaskById(int id) { //удаление задачи по id
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) { //удаление эпика по id
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubTaskById(int id) { //удаление подзадачи по id
        super.removeSubTaskById(id);
        save();
    }

    private String taskToString(Task task) {
        StringBuilder sb = new StringBuilder(task.getId() + "," + TaskType.TASK + "," + task.getName() + "," +
                                            task.getStatus() + "," + task.getDescription());
        return sb.toString();
    }

    private String epicToString(Epic epic) {
        StringBuilder sb = new StringBuilder(epic.getId() + "," + TaskType.EPIC + "," + epic.getName() + "," +
                epic.getStatus() + "," + epic.getDescription());
        return sb.toString();
    }

    private String subtaskToString(SubTask subTask) {
        StringBuilder sb = new StringBuilder(subTask.getId() + "," + TaskType.SUBTASK + "," + subTask.getName() + "," +
                subTask.getStatus() + "," + subTask.getDescription() + "," + subTask.getEpicId());
        return sb.toString();
    }

    private Task fromString(String value) {
        Task tsk = null;
        String[] values = value.split(",");
        switch (values[1]) {
            case "TASK":
                switch(values[3]) {
                    case "NEW":
                        tsk = new Task(Integer.parseInt(values[0]), values[2], values[4]);
                        tasks.put(tsk.getId(), tsk);
                        break;
                    case "IN_PROGRESS":
                        tsk = new Task(Integer.parseInt(values[0]), values[2], values[4], TaskStatus.IN_PROGRESS);
                        tasks.put(tsk.getId(), tsk);
                        break;
                    case "DONE":
                        tsk = new Task(Integer.parseInt(values[0]), values[2], values[4], TaskStatus.DONE);
                        tasks.put(tsk.getId(), tsk);
                        break;
                }
                break;
            case "EPIC":
                switch(values[3]) {
                    case "NEW":
                        tsk = new Epic(Integer.parseInt(values[0]), values[2], values[4]);
                        tasks.put(tsk.getId(), tsk);
                        break;
                    case "IN_PROGRESS":
                        tsk = new Epic(Integer.parseInt(values[0]), values[2], values[4], TaskStatus.IN_PROGRESS);
                        tasks.put(tsk.getId(), tsk);
                        break;
                    case "DONE":
                        tsk = new Epic(Integer.parseInt(values[0]), values[2], values[4], TaskStatus.DONE);
                        tasks.put(tsk.getId(), tsk);
                        break;
                }
                break;
            case "SUBTASK":
                switch(values[3]) {
                    case "NEW":
                        tsk = new SubTask(Integer.parseInt(values[0]), values[2], values[4], Integer.parseInt(values[5]));
                        tasks.put(tsk.getId(), tsk);
                        break;
                    case "IN_PROGRESS":
                        tsk = new SubTask(Integer.parseInt(values[0]), values[2], values[4], TaskStatus.IN_PROGRESS,
                                Integer.parseInt(values[5]));
                        tasks.put(tsk.getId(), tsk);
                        break;
                    case "DONE":
                        tsk = new SubTask(Integer.parseInt(values[0]), values[2], values[4], TaskStatus.DONE,
                                Integer.parseInt(values[5]));
                        tasks.put(tsk.getId(), tsk);
                        break;
                }
                break;
        }
        return tsk;
    }
}
