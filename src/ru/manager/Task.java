package ru.manager;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected TaskStatus status = TaskStatus.NEW;
    protected int id;

    public Task (String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task (String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task (int id, String name, String description) {
        this(name, description);
        this.id = id;
    }
    public Task (int id, String name, String description, TaskStatus status) {
        this(id, name, description);
        this.status = status;
    }

    public Task(Task task) {
        this.id = task.id;
        this.name = task.name;
        this.description = task.description;
        this.status = task.status;
    }

    public int getId() {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public String toString(){
        String result = "Task {" +
                "Id='" + id + '\'' +
                ", name='" + name + '\'';
        if (description == null) {
            result = result + ", description=null" +
                    ", status=" + status.toString() + '}';
        } else {
            result = result + ", description.length=" + description.length() +
                    ", status=" + status.toString() + '}';
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        if (this.id == ((Task) o).id) return true;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) &&
                status == task.status && this.hashCode() == task.hashCode();
    }

    @Override
    public int hashCode () {
        return Objects.hash(name, description, status, id);
    }
}
