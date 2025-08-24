package ru.yandex.javacourse.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String title;
    private String description;
    private Status status;
    private static int count = 0;
    private final int id;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
        id = count++;
        startTime = LocalDateTime.of(1, 1, 1, 0, 0);
        duration = Duration.ofMinutes(0);
    }

    public Task(String title, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.status = status;
        id = count++;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String title, String description, Status status, int id) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.id = id;
        count = id + 1;
        startTime = LocalDateTime.of(1, 1, 1, 0, 0);
        duration = Duration.ofMinutes(0);
    }

    public Task(String title, String description, Status status, int id, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.id = id;
        count = id + 1;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public TaskType getType() {
        if (this instanceof Subtask) {
            return TaskType.SUBTASK;
        } else if (this instanceof Epic) {
            return TaskType.EPIC;
        } else {
            return TaskType.TASK;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.getId());
    }

    @Override
    public int hashCode() {
        return getId();
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

    public void updateStatus(Status newStatus) {
        status = newStatus;
    }
}