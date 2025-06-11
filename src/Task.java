import java.util.Objects;

public class Task {
    protected String title;
    protected String description;
    protected Status status;


    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(title, task.title) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description);
    }

    @Override
    public String toString() {
        String descriptionLength = "null";
        if (description != null) descriptionLength = String.valueOf(description.length());
        return "Task{" +
                "title='" + title + '\'' +
                ", description.length()=" + descriptionLength +
                ", status=" + status +
                ", id=" + this.hashCode() +
                '}';
    }

    public void updateStatus(Status newStatus) {
        status = newStatus;
    }

}
