package ru.taskmanager.tasktypes;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTaskIDs = new ArrayList<>();
    public Epic(int id, String name, String description) {
        super(id, name, description);
    }

    public ArrayList<Integer> getSubTaskIDs() {
        return subTaskIDs;
    }

    public void addSubTaskID(int subTaskID) {
        subTaskIDs.add(subTaskID);
    }

    @Override
    public String toString(){
        String result = "Epic {" +
                "ID='" + ID + '\'' +
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
}
