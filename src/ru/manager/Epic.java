package ru.manager;

import ru.TaskStatus;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTaskIds = new ArrayList<>();
    public Epic(int id, String name, String description) {
        super(id, name, description);
    }

    public ArrayList<Integer> getSubTaskIds() {
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
                "Id='" + Id + '\'' +
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
