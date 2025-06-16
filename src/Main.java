import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.tasks.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // Создаем две задачи
        taskManager.addTask(new Task("Задача 1", "Вот такая задача", Status.NEW));

        Task task = new Task("Задача 2", "Вот такая вторая задача", Status.IN_PROGRESS);
        taskManager.addTask(task);

        // Создаем Эпик с двумя подзадачами
        Epic testEpic = new Epic("Задача Epic", "Вот такая задача",
                new ArrayList<>(), taskManager);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(new Subtask("Подзадача 1", "Вот такая задача", Status.NEW, testEpic.getId()));
        taskManager.addSubtasks(subtasks);

        for (Subtask subtask : subtasks) {
            testEpic.addSubtask(subtask.getId());
        }

        Subtask subtask = new Subtask("Подзадача 2", "Вот такая задача",
                Status.NEW, testEpic.getId());

        testEpic.addSubtask(subtask.getId());
        taskManager.addTask(subtask);

        taskManager.addTask(testEpic);
        taskManager.showTasks();

        // Обновляем статусы
        task.updateStatus(Status.DONE);
        for (int subtaskID : testEpic.getSubtasksIDs()) {
            testEpic.updateSubtaskStatus(subtaskID, Status.DONE);
        }
        taskManager.showTasks();

        // Удаляем Эпик
        taskManager.removeTask(testEpic.getId());
        taskManager.showTasks();

        // Удаляем все Эпики
        taskManager.removeAllTasks();
        taskManager.showTasks();
    }
}