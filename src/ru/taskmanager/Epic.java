package ru.taskmanager;

import java.util.ArrayList;

class Epic extends Task{
    ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description, int id) {
        super(name, description, id);
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }


    public void updateStatus() {

    }

}
