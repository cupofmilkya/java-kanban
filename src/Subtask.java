public class Subtask extends Task {
    private final int epicID;

    public Subtask(String title, String description, Status status, int epicID) {
        super(title, description, status);
        this.epicID = epicID;
    }


    @Override
    public String toString() {
        return "Subtask{" +
                "epicID=" + epicID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + this.hashCode() +
                '}';
    }
}