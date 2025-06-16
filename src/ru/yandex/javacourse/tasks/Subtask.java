package ru.yandex.javacourse.tasks;

public class Subtask extends Task {
    private final int epicID;

    public Subtask(String title, String description, Status status, int epicID) {
        super(title, description, status);
        this.epicID = epicID;
    }


    @Override
    public String toString() {
        String descriptionLength = "null";
        if (super.getDescription() != null) descriptionLength = String.valueOf(super.getDescription().length());
        return "ru.yandex.javacourse.tasks.Subtask{" +
                "epicID=" + epicID +
                ", title='" + super.getTitle() + '\'' +
                ", description.length()=" + descriptionLength +
                ", status=" + super.getStatus() +
                ", id=" + super.getId() +
                '}';
    }
}