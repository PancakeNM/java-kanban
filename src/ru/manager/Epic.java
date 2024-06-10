package ru.manager;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTaskIds;

    public Epic(String name, String description) {
        super(name, description);
        subTaskIds = new ArrayList<>();
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
        subTaskIds = new ArrayList<>();
    }

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
        subTaskIds = new ArrayList<>();
    }

    public Epic(Epic epic) {
       super(epic.id, epic.name, epic.description);
       this.subTaskIds = epic.subTaskIds;
    }

    public List<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void addSubTaskId(int subtaskId) {
        if (id != subtaskId) {
            subTaskIds.add(subtaskId);
        }
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void removeSubTaskId(Integer subTaskId) {
        subTaskIds.remove(subTaskId);
    }

    public void removeAllSubTaskId() {
        subTaskIds.clear();
    }

    @Override
    public String toString() {
        String result = "Epic {" +
                "Id='" + id + '\'' +
                ", name='" + name + '\'';
        if (description == null) {
            result = result + ", description=null" +
                    ", status=" + status.toString() + '}';
        } else {
            result = result + ", description.length=" + description.length() +
                    ", status=" + status.toString() +
                    ", subTaskIds=" + subTaskIds;
        }
        result = result + "startTime='" + startTime.toString() + '\'' +
                "duration='" + duration.toString() + '\'' +
                "endTime='" + endTime.toString() + '}';
        return result;
    }
}
