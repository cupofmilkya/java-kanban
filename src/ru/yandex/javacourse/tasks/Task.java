package ru.yandex.javacourse.tasks;

import java.util.Objects;

public class Task {
    private String title;
    private String description;
    private Status status;
    private static int count = 0;
    private final int id;

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
        id = count++;
    }

    public Task(String title, String description, Status status, int id) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.id = id;
        count = id + 1;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        String descriptionLength = "null";
        if (description != null) descriptionLength = String.valueOf(description.length());
        return "Task{" +
                "title='" + title + '\'' +
                ", description.length()=" + descriptionLength +
                ", status=" + status +
                ", id=" + getId() +
                '}';
    }

    public void updateStatus(Status newStatus) {
        status = newStatus;
    }
}