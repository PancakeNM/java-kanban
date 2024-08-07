package ru.manager;

import ru.Managers;
import ru.manager.interfaces.HistoryManager;
import ru.manager.utility.ManagerIOException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private Path file;

    public FileBackedTaskManager(HistoryManager historyManager, Path file) {
        super(historyManager);
        this.file = file;
    }

    public void save() throws ManagerIOException {
        List<String> lines = new ArrayList<>();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.toFile(), StandardCharsets.UTF_8))) {
            if (!tasks.isEmpty()) {
                for (Task task : tasks.values()) {
                    lines.add(taskToString(task));
                }
            }
            if (!epics.isEmpty()) {
                for (Epic epic : epics.values()) {
                    lines.add(epicToString(epic));
                }
            }
            if (!subTasks.isEmpty()) {
                for (SubTask subtask : subTasks.values()) {
                    lines.add(subtaskToString(subtask));
                }
            }
            bw.write("id,type,name,status,description,epic,duration,startTime");
            bw.newLine();
            for (String str : lines) {
                bw.write(str + "\n");
            }
        } catch (IOException exp) {
            throw new ManagerIOException("Ошибка записи в файл.");
        }
    }

    static FileBackedTaskManager loadFromFile(File file) throws ManagerIOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(Managers.getDefaultHistoryManager(), file.toPath());
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                lines.add(br.readLine());
            }
        } catch (IOException e) {
            throw new ManagerIOException("Ошибка чтения файла.");
        }
        for (int i = 1; i < lines.size(); i++) {
            if (manager.taskFromString(lines.get(i)) != null) {
                String[] values = lines.get(i).split(",");
                switch (values[1]) {
                    case "TASK":
                        Task newTask = new Task(manager.taskFromString(lines.get(i)));
                        manager.tasks.put(newTask.getId(), newTask);
                        break;
                    case "EPIC":
                        Epic newEpic = new Epic(manager.epicFromString(lines.get(i)));
                        manager.epics.put(newEpic.getId(), newEpic);
                        break;
                    case "SUBTASK":
                        SubTask newSubtask = new SubTask(manager.subtaskFromString(lines.get(i)));
                        manager.subTasks.put(newSubtask.getId(), newSubtask);
                        break;
                }
            }
        }
        manager.setId(lines.size() - 1);
        return manager;
    }

    @Override
    public void addNewTask(Task task) { //метод добавления новой задачи.
        super.addNewTask(task);
        try {
            save();
        } catch (ManagerIOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addNewSubTask(SubTask subTask) { //метод добавления новой подзадачи.
        super.addNewSubTask(subTask);
        try {
            save();
        } catch (ManagerIOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        try {
            save();
        } catch (ManagerIOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        try {
            save();
        } catch (ManagerIOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        try {
            save();
        } catch (ManagerIOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addNewEpic(Epic epic) { //метод добавления нового эпика.
        super.addNewEpic(epic);
        try {
            save();
        } catch (ManagerIOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllTasks() { //удаление всех задач
        super.removeAllTasks();
        try {
            save();
        } catch (ManagerIOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllEpics() { //удаление всех эпиков
        super.removeAllEpics();
        try {
            save();
        } catch (ManagerIOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllSubTasks() { //удаление всех подзадач
        super.removeAllSubTasks();
        try {
            save();
        } catch (ManagerIOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeTaskById(int id) { //удаление задачи по id
        super.removeTaskById(id);
        try {
            save();
        } catch (ManagerIOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeEpicById(int id) { //удаление эпика по id
        super.removeEpicById(id);
        try {
            save();
        } catch (ManagerIOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeSubTaskById(int id) { //удаление подзадачи по id
        super.removeSubTaskById(id);
        try {
            save();
        } catch (ManagerIOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String taskToString(Task task) {
        StringBuilder sb = new StringBuilder(task.getId() + "," + TaskType.TASK + "," + task.getName() + "," +
                                            task.getStatus() + "," + task.getDescription()
                                            + task.getDuration() + "," + task.getStartTime());
        return sb.toString();
    }

    private String epicToString(Epic epic) {
        StringBuilder sb = new StringBuilder(epic.getId() + "," + TaskType.EPIC + "," + epic.getName() + "," +
                epic.getStatus() + "," + epic.getDescription() + epic.getDuration() + "," + epic.getStartTime());
        return sb.toString();
    }

    private String subtaskToString(SubTask subTask) {
        StringBuilder sb = new StringBuilder(subTask.getId() + "," + TaskType.SUBTASK + "," + subTask.getName() + ","
                + subTask.getStatus() + "," + subTask.getDescription() + "," + subTask.getEpicId() + ","
                + subTask.getDuration() + "," + subTask.getStartTime());
        return sb.toString();
    }

    private Task taskFromString(String value) {
        Task tsk;
        String[] values = value.split(",");
        switch (values[3]) {
            case "NEW":
                tsk = new Task(Integer.parseInt(values[0]), values[2], values[4]);
                return tsk;
            case "IN_PROGRESS":
                tsk = new Task(Integer.parseInt(values[0]), values[2], values[4], TaskStatus.IN_PROGRESS);
                return tsk;
            case "DONE":
                tsk = new Task(Integer.parseInt(values[0]), values[2], values[4], TaskStatus.DONE);
                return tsk;
            default:
                return null;
        }
    }

    private Epic epicFromString(String value) {
        Epic tsk;
        String[] values = value.split(",");
        switch (values[3]) {
            case "NEW":
                tsk = new Epic(Integer.parseInt(values[0]), values[2], values[4]);
                return tsk;
            case "IN_PROGRESS":
                tsk = new Epic(Integer.parseInt(values[0]), values[2], values[4], TaskStatus.IN_PROGRESS);
                return tsk;
            case "DONE":
                tsk = new Epic(Integer.parseInt(values[0]), values[2], values[4], TaskStatus.DONE);
                return tsk;
            default:
                return null;
        }
    }

    private SubTask subtaskFromString(String value) {
        SubTask tsk;
        String[] values = value.split(",");
        switch (values[3]) {
            case "NEW":
                tsk = new SubTask(Integer.parseInt(values[0]), values[2], values[4],
                        Integer.parseInt(values[5]));
                return tsk;
            case "IN_PROGRESS":
                tsk = new SubTask(Integer.parseInt(values[0]), values[2], values[4], TaskStatus.IN_PROGRESS,
                        Integer.parseInt(values[5]));
                return tsk;
            case "DONE":
                tsk = new SubTask(Integer.parseInt(values[0]), values[2], values[4], TaskStatus.DONE,
                        Integer.parseInt(values[5]));
                return tsk;
            default:
                return null;
        }
    }
}
