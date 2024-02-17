package ru.taskmanager;

import ru.taskmanager.tasktypes.Epic;
import ru.taskmanager.tasktypes.SubTask;
import ru.taskmanager.tasktypes.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task(taskManager.generateNewId(),"Задача 1", "Тестовая 1");
        Task task2 = new Task(taskManager.generateNewId(), "Задача 2", null);

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.removeTaskById(1);

        System.out.println(taskManager.getTaskById(2).toString());

        Epic epic1 = new Epic(taskManager.generateNewId(), "Эпик 1", "Тест");
        Epic epic2 = new Epic(taskManager.generateNewId(), "Эпик 2", "Тест");

        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);
        taskManager.removeEpicById(3);

        System.out.println(taskManager.getEpicById(4).toString());

        SubTask subTask1 = new SubTask(taskManager.generateNewId(), "Подзадача 1", "Тест");
        SubTask subTask2 = new SubTask(taskManager.generateNewId(), "Подзадача 2", "Тест");

        taskManager.addNewSubTask(subTask1, 4);
        taskManager.addNewSubTask(subTask2, 4);
        taskManager.removeSubTaskById(5);



        taskManager.removeAllTasks();
        taskManager.removeAllSubTasks();
        taskManager.removeAllEpics();
    }
}
