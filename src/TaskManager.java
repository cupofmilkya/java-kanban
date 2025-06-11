import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks;

    public TaskManager() {
        tasks = new HashMap<>();
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void showTasks() {
        System.out.println();
        for (Task task : tasks.values()) {
            if (task.getStatus() == Status.NEW) System.out.println(task);
        }
        for (Task task : tasks.values()) {
            if (task.getStatus() == Status.IN_PROGRESS) System.out.println(task);
        }
        for (Task task : tasks.values()) {
            if (task.getStatus() == Status.DONE) System.out.println(task);
        }
    }

    public void addTask(Task task) {
        tasks.put(task.hashCode(), task);
    }

}