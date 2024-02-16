package ru.taskmanager;

public class Task {
    private String name;
    private String description;
    private int id;
    private TaskStatus status = TaskStatus.NEW;

    public Task (String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public void updateStatus(TaskStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }
}
