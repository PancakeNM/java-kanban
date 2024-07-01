package ru.manager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected TaskStatus status = TaskStatus.NEW;
    protected int id;
    LocalDateTime startTime; //LocalDateTime в рабочих проектах не использовать!
    Duration duration;
    LocalDateTime endTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.startTime = LocalDateTime.now();
        this.duration = Duration.ofMinutes(10);
        this.endTime = startTime.plus(duration);
    }

    public Task(String name, String description, TaskStatus status) {
        this(name, description);
        this.status = status;
    }

    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plus(duration);
    }

    public Task(String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this(name, description, startTime, duration);
        this.status = status;
    }

    public Task(int id, String name, String description) {
        this(name, description);
        this.id = id;
    }

    public Task(int id, String name, String description, TaskStatus status) {
        this(id, name, description);
        this.status = status;
    }

    public Task(Task task) {
        this.id = task.id;
        this.name = task.name;
        this.description = task.description;
        this.status = task.status;
        this.startTime = task.getStartTime();
        this.duration = task.getDuration();
        this.endTime = task.getEndTime();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        recalculateEndTime();
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
        recalculateEndTime();
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    private void recalculateEndTime() {
        this.endTime = startTime.plus(duration);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String result = "Task {" +
                "Id='" + id + '\'' +
                ", name='" + name + '\'';
        if (description == null) {
            result = result + ", description=null" +
                    ", status=" + status.toString();
        } else {
            result = result + ", description.length=" + description.length() +
                    ", status=" + status.toString();
        }
        result = result + "startTime='" + startTime.toString() + '\'' +
        "duration='" + duration.toString() + '\'' +
        "endTime='" + endTime.toString() + '}';
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
    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }
}
