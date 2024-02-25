package ru;

import ru.manager.Epic;
import ru.manager.SubTask;
import ru.manager.Task;
import ru.manager.TaskManager;

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

        SubTask subTask1 = new SubTask(taskManager.generateNewId(), "Подзадача 1", "Тест", 4);
        SubTask subTask2 = new SubTask(taskManager.generateNewId(), "Подзадача 2", "Тест", 4);

        taskManager.addNewSubTask(subTask1);
        taskManager.addNewSubTask(subTask2);
        taskManager.removeSubTaskById(5);

        System.out.println(taskManager.getSubTaskById(6));

        System.out.println(taskManager.getEpicById(4).toString());

        subTask2 = new SubTask(subTask2.getId(), "Тест изменения", "ТестТест", TaskStatus.DONE,
                subTask2.getEpicId());

        taskManager.updateSubTask(subTask2);

        System.out.println(taskManager.getSubTaskById(6));

        System.out.println(taskManager.getEpicById(4).toString());

        SubTask subTask3 = new SubTask(taskManager.generateNewId(), "Подзадача 3", "Тест", 4);

        taskManager.addNewSubTask(subTask3);

        System.out.println(taskManager.getSubTasksByEpicId(4));
        System.out.println(taskManager.getEpicById(4).toString());

        taskManager.removeAllTasks();
        taskManager.removeAllSubTasks();
        taskManager.removeAllEpics();
    }
}
