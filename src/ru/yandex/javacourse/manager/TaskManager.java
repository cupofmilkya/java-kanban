package ru.yandex.javacourse.manager;
import ru.yandex.javacourse.tasks.*;

import java.util.HashMap;
import java.util.ArrayList;


public class TaskManager {
    HashMap<Integer, Task> tasks;

    public TaskManager() {
        tasks = new HashMap<>();
    }

    public HashMap<Integer, Task> getAllTasks() {
        return tasks;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> result = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (!(task instanceof Subtask) && !(task instanceof Epic)) {
                result.add(task);
            }
        }
        return result;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> result = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task instanceof Epic) {
                result.add((Epic) task);
            }
        }
        return result;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> result = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task instanceof Subtask) {
                result.add((Subtask) task);
            }
        }
        return result;
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeTask(int taskToRemove) {
        Epic epicToDelete = null;
        for (Task task : tasks.values()) {
            if (task.getId() == taskToRemove && task instanceof Epic) {
                epicToDelete = (Epic) task;
            }
        }
        if (epicToDelete == null) {
            tasks.remove(taskToRemove);
            return;
        }

        for (int id : epicToDelete.getSubtasksIDs()) {
            for (Task task : tasks.values()) {
                if (task.getId() == taskToRemove && task instanceof Subtask) {
                    tasks.remove(id);
                    continue;
                }
            }
        }
        tasks.remove(taskToRemove);
    }

    public void showTasks() {
        System.out.println();
        if (tasks.isEmpty()) {
            System.out.println("Список пустой");
            return;
        }

        System.out.println("Tasks: " + getTasks());
        System.out.println("Epics: " + getEpics());
        System.out.println("Subtasks: " + getSubtasks());
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void addSubtasks(ArrayList<Subtask> tasks) {
        if (tasks.isEmpty()) return;

        for (Subtask task : tasks) {
            addTask(task);
        }
    }
}