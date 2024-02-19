package ru.manager;

import ru.TaskStatus;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected TaskStatus status = TaskStatus.NEW;
    protected final int Id;

    public Task (int id, String name, String description) {
        this.name = name;
        this.description = description;
        this.Id = id;
    }
    public Task (int id, String name, String description, TaskStatus status) {
        this(id, name, description);
        this.status = status;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return name;
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
                "Id='" + Id + '\'' +
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
        if (this.Id == ((Task) o).Id) return true;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, Id);
    }
}
