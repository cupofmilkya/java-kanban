package ru.yandex.javacourse.tasks;

import java.util.Objects;

public class Subtask extends Task {
    private final int epicID;

    public Subtask(String title, String description, Status status, int epicID) {
        super(title, description, status);
        this.epicID = epicID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(super.getId(), task.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId());
    }

    @Override
    public String toString() {
        String descriptionLength = "null";
        if (super.getDescription() != null) descriptionLength = String.valueOf(super.getDescription().length());
        return "Subtask{" +
                "epicID=" + epicID +
                ", title='" + super.getTitle() + '\'' +
                ", description.length()=" + descriptionLength +
                ", status=" + super.getStatus() +
                ", id=" + super.getId() +
                '}';
    }
}