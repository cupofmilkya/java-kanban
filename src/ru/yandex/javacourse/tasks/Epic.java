package ru.yandex.javacourse.tasks;

import ru.yandex.javacourse.exceptions.manager.NotFoundException;
import ru.yandex.javacourse.manager.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIDs;
    private transient TaskManager taskManager;
    private LocalDateTime endTime;

    public Epic(String title, String description, ArrayList<Integer> subtasksIDs,
                TaskManager taskManager) {
        super(title, description, Status.NEW);
        this.subtasksIDs = subtasksIDs;
        this.taskManager = taskManager;
        updateStatus();
        updateDuration();
    }

    public Epic(String title, String description,
                TaskManager taskManager) {
        super(title, description, Status.NEW);
        this.subtasksIDs = new ArrayList<>();
        this.taskManager = taskManager;
        updateStatus();
        updateDuration();
    }

    public Epic(String title, String description, ArrayList<Integer> subtasksIDs,
                int id, Duration duration, LocalDateTime startTime) {
        super(title, description, Status.NEW, id, duration, startTime);
        this.subtasksIDs = subtasksIDs;
        updateStatus();
        updateDuration();
    }

    public Epic(String title, String description, ArrayList<Integer> subtasksIDs,
                int id) {
        super(title, description, Status.NEW, id);
        this.subtasksIDs = subtasksIDs;
        updateStatus();
        updateDuration();
    }

    public ArrayList<Integer> getSubtasksIDs() {
        return subtasksIDs;
    }

    @Override
    public LocalDateTime getEndTime() {
        updateDuration();
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public Status getStatus() {
        updateStatus();
        return super.getStatus();
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void setSubtasks(ArrayList<Integer> subtasksIDs) {
        this.subtasksIDs = subtasksIDs;
        updateStatus();
        updateDuration();
    }

    public void addSubtask(int subtaskID) {
        if (subtaskID == this.getId()) return;
        if (subtasksIDs.contains(subtaskID)) return;

        for (Task task : taskManager.getTasks()) {
            if (subtaskID == task.getId() && !(task instanceof Subtask)) {
                return;
            }
        }
        subtasksIDs.add(subtaskID);
        updateStatus();
        updateDuration();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(super.getId(), task.getId());
    }

    @Override
    public String toString() {
        return getId() +
                "," + getType() +
                "," + getTitle() +
                "," + getStatus() +
                "," + getDescription() +
                "," + getStartTime() +
                "," + getDuration();
    }

    @Override
    public void updateStatus(Status newStatus) {
        System.out.println("В Epic нельзя обновить статус, работайте с Subtask");
        System.out.println("Подзадачи: " + subtasksIDs.toString());
    }

    @Override
    public Duration getDuration() {
        updateDuration();
        return super.getDuration();
    }

    public void updateSubtaskStatus(int subtaskId, Status newStatus) {
        for (Task task : taskManager.getSubtasks()) {
            if (task.getId() == subtaskId) {
                task.updateStatus(newStatus);
                break;
            }
        }
        updateStatus();
        updateDuration();
    }

    public void showSubtasks() {
        System.out.println();
        System.out.println("ID подзадач Эпика \"" + super.getTitle() + "\", ID: " + super.getId());
        for (int subtask : subtasksIDs) {
            System.out.print(subtask + " ");
        }
        System.out.println();
    }

    private void updateStatus() {
        int newCount = 0;
        int doneCount = 0;

        if (subtasksIDs == null || subtasksIDs.isEmpty()) {
            super.setStatus(Status.NEW);
            return;
        }


        newCount = (int) subtasksIDs.stream()
                .map(id -> {
                    try {
                        return taskManager.getTask(id);
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(task -> task instanceof Subtask)
                .filter(task -> task.getStatus() == Status.NEW)
                .count();

        doneCount = (int) subtasksIDs.stream()
                .map(id -> {
                    try {
                        return taskManager.getTask(id);
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(task -> task instanceof Subtask)
                .filter(task -> task.getStatus() == Status.DONE)
                .count();


        if (newCount == subtasksIDs.size()) {
            super.setStatus(Status.NEW);
        } else if (doneCount == subtasksIDs.size()) {
            super.setStatus(Status.DONE);
        } else {
            super.setStatus(Status.IN_PROGRESS);
        }
    }

    private void updateDuration() {
        List<Subtask> epicSubtasks = subtasksIDs.stream()
                .map(id -> {
                    try {
                        return taskManager.getTask(id);
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(task -> task instanceof Subtask)
                .map(task -> (Subtask) task)
                .collect(Collectors.toList());

        if (epicSubtasks.isEmpty()) {
            super.setStartTime(LocalDateTime.of(1, 1, 1, 0, 0));
            super.setDuration(Duration.ZERO);
            setEndTime(LocalDateTime.of(1, 1, 1, 0, 0));
            return;
        }

        LocalDateTime start = epicSubtasks.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.of(1, 1, 1, 0, 0));

        LocalDateTime end = epicSubtasks.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(start);

        Duration duration = epicSubtasks.stream()
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);

        super.setStartTime(start);
        super.setDuration(duration);
        setEndTime(end);
    }
}