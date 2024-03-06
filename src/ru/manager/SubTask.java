package ru.manager;

public class SubTask extends Task {
    private int epicId;
    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, String description, int epicId) {
        super(id, name, description);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, String description, TaskStatus status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public SubTask(SubTask subTask) {
        super(subTask.id, subTask.name, subTask.description, subTask.status);
        this.epicId = subTask.getEpicId();
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        if (id != epicId) {
            this.epicId = epicId;
        }
    }

    @Override
    public String toString(){
        String result = "SubTask {" +
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
}
