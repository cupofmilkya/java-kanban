package ru.yandex.javacourse.tasks;
import ru.yandex.javacourse.manager.InMemoryTaskManager;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task{
    private ArrayList<Integer> subtasksIDs;
    private final InMemoryTaskManager inMemoryTaskManager;

    public Epic(String title, String description, ArrayList<Integer> subtasksIDs, InMemoryTaskManager inMemoryTaskManager) {
        super(title, description, Status.NEW);
        this.subtasksIDs = subtasksIDs;
        this.inMemoryTaskManager = inMemoryTaskManager;
        updateStatus();
    }

    public ArrayList<Integer> getSubtasksIDs() {
        return subtasksIDs;
    }

    @Override
    public Status getStatus() {
        updateStatus();
        return super.getStatus();
    }

    public void setSubtasks(ArrayList<Integer> subtasksIDs) {
        this.subtasksIDs = subtasksIDs;
        updateStatus();
    }

    public void addSubtask(int subtaskID) {
        if (subtaskID == this.getId()) return;
        if (subtasksIDs.contains(subtaskID)) return;

        for (Task task : inMemoryTaskManager.getTasks()) {
            if(subtaskID == task.getId() && !(task instanceof Subtask)) {
                return;
            }
        }
        subtasksIDs.add(subtaskID);
        updateStatus();
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
        return "Epic{" +
                "title='" + super.getTitle() + '\'' +
                ", description.length()=" + descriptionLength +
                ", status=" + super.getStatus() + ", " +
                "subtasks=" + subtasksIDs +
                ", id=" + super.getId() +
                '}';
    }

    @Override
    public void updateStatus(Status newStatus) {
        System.out.println("В Epic нельзя обновить статус, работайте с Subtask");
        System.out.println("Подзадачи: " + subtasksIDs.toString());
    }

    public void updateSubtaskStatus(int subtaskId, Status newStatus) {
        for (Task task : inMemoryTaskManager.getSubtasks()) {
            if (task.getId() == subtaskId) {
                task.updateStatus(newStatus);
                break;
            }
        }
        updateStatus();
    }

    public void showSubtasks() {
        System.out.println();
        System.out.println("ID подзадач Эпика \"" + super.getTitle() + "\", ID: " + super.getId());
        for (int subtask : subtasksIDs) {
            System.out.print(subtask + " ");
        }
        System.out.println();
    }

    private void updateStatus(){
        int newCount = 0;
        int doneCount = 0;

        if (subtasksIDs == null || subtasksIDs.isEmpty()) {
            super.setStatus(Status.NEW);
            return;
        }

        for (int subtask : subtasksIDs) {
            for (Subtask task : inMemoryTaskManager.getSubtasks()) {
                if (!(task.getId() == subtask)) continue;

                if (task.getStatus() == Status.NEW) newCount++;
                if (task.getStatus() == Status.DONE) doneCount++;
            }
        }

        if (newCount == subtasksIDs.size()) {
            super.setStatus(Status.NEW);
        } else if (doneCount == subtasksIDs.size()) {
            super.setStatus(Status.DONE);
        } else {
            super.setStatus(Status.IN_PROGRESS);
        }
    }
}
