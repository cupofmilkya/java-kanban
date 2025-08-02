package ru.yandex.javacourse.tasks;

import java.util.Objects;

public class Subtask extends Task {
    private int epicID = -1;

    public Subtask(String title, String description, Status status, int epicID) {
        super(title, description, status);

        if (epicID != this.getId()) {
            this.epicID = epicID;
        } else {
            throw new IllegalArgumentException("EpicID is already set to this id");
        }
    }

    public Subtask(String title, String description, Status status, int epicID, int id) {
        super(title, description, status, id);
        if (epicID != this.getId()) {
            this.epicID = epicID;
        } else {
            throw new IllegalArgumentException("EpicID is already set to this id");
        }
    }

    public Subtask(String title, String description, Status status) {
        super(title, description, status);
    }

    public void setEpicID(int epicID) {
        if (epicID != this.getId()) {
            this.epicID = epicID;
        }
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
                "," + getEpicID();
    }

    public int getEpicID() {
        return epicID;
    }
}