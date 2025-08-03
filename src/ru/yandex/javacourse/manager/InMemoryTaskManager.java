package ru.yandex.javacourse.manager;

import ru.yandex.javacourse.tasks.*;

import java.util.HashMap;
import java.util.ArrayList;


public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks;
    final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        tasks = new HashMap<>();
        this.historyManager = historyManager;
    }

    @Override
    public HashMap<Integer, Task> getAllTasks() {
        return tasks;
    }

    @Override
    public Task getTask(int id) {
        for (Task task : tasks.values()) {
            if (task.getId() == id) {
                historyManager.add(task);
                return task;
            }
        }
        return null;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> result = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (!(task instanceof Subtask) && !(task instanceof Epic)) {
                result.add(task);
                historyManager.add(task);
            }
        }
        return result;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> result = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task instanceof Epic) {
                result.add((Epic) task);
                historyManager.add(task);
            }
        }
        return result;
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        if (!(tasks.get(epicId) instanceof Epic epic)) return null;

        ArrayList<Subtask> subtasks = new ArrayList<>();

        for (int id : epic.getSubtasksIDs()) {
            Task task = tasks.get(id);
            subtasks.add((Subtask) task);
            historyManager.add(task);
        }

        return subtasks;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> result = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task instanceof Subtask) {
                result.add((Subtask) task);
                historyManager.add(task);
            }
        }
        return result;
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
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
                }
            }
        }
        tasks.remove(taskToRemove);
    }

    @Override
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

    @Override
    public void addTask(Task task) {
        if (task == null) {
            System.out.println("Ошибка: задача не может быть null");
            return;
        }

        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            int epicId = subtask.getEpicID();

            if (!tasks.containsKey(epicId)) {
                System.out.println("Ошибка: эпик с ID=" + epicId + " не найден");
                return;
            }

            Task epic = tasks.get(epicId);
            if (!(epic instanceof Epic)) {
                System.out.println("Ошибка: задача с ID=" + epicId + " не является эпиком");
                return;
            }

            ((Epic) epic).addSubtask(subtask.getId());
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void addSubtasks(ArrayList<Subtask> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Пропущена строка (передан null)");
        }

        for (Subtask task : tasks) {
            addTask(task);
        }
    }

    public HistoryManager getInMemoryHistory() {
        return historyManager;
    }
}