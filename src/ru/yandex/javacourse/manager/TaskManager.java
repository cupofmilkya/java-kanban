package ru.yandex.javacourse.manager;

import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.Subtask;
import ru.yandex.javacourse.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {
    HashMap<Integer, Task> getAllTasks();


    Task getTask(int id);

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getEpicSubtasks(int epicId);

    ArrayList<Subtask> getSubtasks();


    void removeAllTasks();

    void removeTask(int taskToRemove);

    void showTasks();

    void addTask(Task task);

    void addSubtasks(ArrayList<Subtask> tasks);
}