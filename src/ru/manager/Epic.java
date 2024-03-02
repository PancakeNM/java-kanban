package ru.manager;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTaskIds = new ArrayList<>();
    public Epic(String name, String description) {
        super(name, description);
    }

    public List<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void addSubTaskId(int Id) {
        subTaskIds.add(Id);
    }
    public void removeSubTaskId(Integer Id) {
        subTaskIds.remove(Id);
    }
    public void removeAllSubTaskId(){
        subTaskIds.clear();
    }

    protected void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString(){
        String result = "Epic {" +
                "Id='" + id + '\'' +
                ", name='" + name + '\'';
        if (description == null) {
            result = result + ", description=null" +
                    ", status=" + status.toString() + '}';
        } else {
            result = result + ", description.length=" + description.length() +
                    ", status=" + status.toString() +
                    ", subTaskIds=" + subTaskIds  + '}';
        }
        return result;
    }
}
