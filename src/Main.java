import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.addTask(new Task("Задача 1", "Вот такая задача", Status.NEW));
        taskManager.addTask(new Task("Задача 2", "Вот такая вторая задача", Status.IN_PROGRESS));

        Epic testEpic = new Epic("Задача Epic", "Вот такая задача", Status.NEW, new ArrayList<>());
        ArrayList<Subtask> subtasks = new ArrayList<Subtask>();
        subtasks.add(new Subtask("Подзадача 1", "Вот такая задача", Status.NEW, testEpic.hashCode()));
        subtasks.add(new Subtask("Подзадача 2", "Вот такая задача", Status.NEW, testEpic.hashCode()));
        testEpic.setSubtasks(subtasks);

        testEpic.addSubtask(new Subtask("Подзадача 3", "Вот такая задача",
                Status.NEW, testEpic.hashCode()));

        taskManager.addTask(testEpic);

        taskManager.showTasks();
    }
}
