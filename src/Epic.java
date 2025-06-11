import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Subtask> subtasks;

    public Epic(String title, String description, Status status, ArrayList<Subtask> subtasks) {
        super(title, description, status);
        this.subtasks = subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    @Override
    public void updateStatus(Status newStatus) {
        System.out.println("В Epic нельзя обновить статус, работайте с Subtask");
        System.out.println("Подзадачи: " + subtasks.toString());
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
                '}';
    }

    public void updateSubtasks(int subtaskId, Status newStatus) {

    }
}
