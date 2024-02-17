package ru.taskmanager.tasktypes;

public class SubTask extends Task{
    public SubTask(int id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public String toString(){
        String result = "SubTask {" +
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
