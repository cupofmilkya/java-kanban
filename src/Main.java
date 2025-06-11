import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // Создаем две задачи
        taskManager.addTask(new Task("Задача 1", "Вот такая задача", Status.NEW));

        Task task = new Task("Задача 2", "Вот такая вторая задача", Status.IN_PROGRESS);
        taskManager.addTask(task);

        // Создаем Эпик с двумя подзадачами
        Epic testEpic = new Epic("Задача Epic", "Вот такая задача", Status.NEW, new ArrayList<>());
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(new Subtask("Подзадача 1", "Вот такая задача", Status.NEW, testEpic.hashCode()));
        testEpic.setSubtasks(subtasks);
        testEpic.addSubtask(new Subtask("Подзадача 2", "Вот такая задача",
                Status.NEW, testEpic.hashCode()));
        // Вывод подзадач testEpic
        testEpic.showSubtasks();

        taskManager.addTask(testEpic);

        taskManager.showTasks();

        // Обновляем статусы
        task.updateStatus(Status.DONE);
        for (Subtask subtask : subtasks) {
            testEpic.updateSubtaskStatus(subtask.hashCode(), Status.DONE);
        }
        taskManager.showTasks();

        // Удаляем Эпик
        taskManager.removeTask(testEpic);
        taskManager.showTasks();

        // Удаляем все Эпики

        taskManager.removeAllTasks();
        taskManager.showTasks();
    }
}
