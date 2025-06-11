import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Subtask> subtasks;

    public Epic(String title, String description, Status status, ArrayList<Subtask> subtasks) {
        super(title, description, status);
        this.subtasks = subtasks;
        updateStatus();
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
        updateStatus();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        updateStatus();
    }

    @Override
    public String toString() {
        String descriptionLength = "null";
        if (description != null) descriptionLength = String.valueOf(description.length());
        return "Epic{" +
                "title='" + title + '\'' +
                ", description.length()=" + descriptionLength +
                ", status=" + status + ", " +
                "subtasks=" + subtasks +
                ", id=" + this.hashCode() +
                '}';
    }

    @Override
    public void updateStatus(Status newStatus) {
        System.out.println("В Epic нельзя обновить статус, работайте с Subtask");
        System.out.println("Подзадачи: " + subtasks.toString());
    }


    public void updateSubtaskStatus(int subtaskId, Status newStatus) {
        for (Subtask subtask : subtasks) {
            if (subtask.hashCode() == subtaskId) {
               subtask.updateStatus(newStatus);
               break;
            }
        }
        updateStatus();
    }

    public void showSubtasks() {
        System.out.println();
        System.out.println("Подзачачи Эпика \"" + title + "\"");
        for (Subtask subtask : subtasks) {
            System.out.println(subtask);
        }
    }

    private void updateStatus(){
        int newCount = 0;
        int doneCount = 0;

        for (Subtask subtask : subtasks) {
            if (subtask.status == Status.NEW) newCount++;
            if (subtask.status == Status.DONE) doneCount++;
        }

        if (newCount == subtasks.size()) {
            this.status = Status.NEW;
        } else if (doneCount == subtasks.size()) {
            this.status = Status.DONE;
        } else {
            this.status = Status.IN_PROGRESS;
        }
    }
}
