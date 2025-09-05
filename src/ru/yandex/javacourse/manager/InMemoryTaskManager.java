package ru.yandex.javacourse.manager;

import ru.yandex.javacourse.exceptions.manager.TimeOverlapConflictException;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.Subtask;
import ru.yandex.javacourse.tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;


public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks;

    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime)
    );

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
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
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

        prioritizedTasks.removeIf(task -> task.getId() == taskToRemove);
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

        if (hasTimeOverlap(task) &&
                !task.getStartTime().isEqual(LocalDateTime.of(1, 1, 1, 0, 0))) {
            throw new TimeOverlapConflictException("Задача пересекается по времени с существующими задачами");
        }

        if (task.getId() == null) {
            task.setId();
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

        if (!task.getStartTime().isEqual(LocalDateTime.of(1, 1, 1, 0, 0))) {
            prioritizedTasks.add(task);
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return;
        }

        Task taskToUpdate = tasks.get(task.getId());

        taskToUpdate.setTitle(task.getTitle());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setStatus(task.getStatus());
        taskToUpdate.setDuration(task.getDuration());
        taskToUpdate.setStartTime(task.getStartTime());
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


    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public boolean isPairOfTasksOverlap(Task task1, Task task2) {
        if (task1 == null || task2 == null) return false;

        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        return !(end1.isBefore(start2)) && !(end2.isBefore(start1));
    }

    public boolean hasTimeOverlap(Task newTask) {
        if (newTask == null) return false;

        return getAllTasks().values().stream()
                .filter(task -> task.getId() != newTask.getId())
                .anyMatch(existingTask -> isPairOfTasksOverlap(existingTask, newTask));
    }
}