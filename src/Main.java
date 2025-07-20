import ru.yandex.javacourse.collections.Node;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.manager.Managers;
import ru.yandex.javacourse.tasks.*;

import java.util.ArrayList;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();

        // Создаем три задачи
        inMemoryTaskManager.addTask(new Task("Задача 1", "Вот такая задача", Status.NEW));

        Task task2 = new Task("Задача 2", "Вот такая вторая задача", Status.IN_PROGRESS);
        inMemoryTaskManager.addTask(task2);

        Task task3 = new Task("Задача 3", "Вот такая третья задача", Status.DONE);
        inMemoryTaskManager.addTask(task3);

        // Создаем эпик и сабтаски
        Epic epic = new Epic("Задача Эпик", "Вот такой эпик", new ArrayList<>(), inMemoryTaskManager);
        Subtask subtask1 = new Subtask("Задача сабтаск1", "Вот такой первый сабтаск", Status.NEW,
                epic.getId());
        Subtask subtask2 = new Subtask("Задача сабтаск2", "Вот такой второой сабтаск", Status.NEW,
                epic.getId());

        epic.addSubtask(subtask1.getId());
        epic.addSubtask(subtask2.getId());
        inMemoryTaskManager.addTask(epic);
        inMemoryTaskManager.addTask(subtask1);
        inMemoryTaskManager.addTask(subtask2);


        // Используем геттеры и выводим таски
        System.out.println("Задачи:");
        for (Task taskDefault : inMemoryTaskManager.getTasks()) {
            System.out.println(taskDefault);
        }
        System.out.println("Эпики:");
        for (Epic taskEpic : inMemoryTaskManager.getEpics()) {
            System.out.println(taskEpic);

            System.out.println("  Подзадачи Эпика:");
            for (Subtask task : inMemoryTaskManager.getEpicSubtasks(epic.getId())) {
                System.out.println("  --> " + task);
            }
        }

        System.out.println("Подзадачи:");
        for (Task subtask : inMemoryTaskManager.getSubtasks()) {
            System.out.println(subtask);
        }

        // Проверка истории
        System.out.println();
        System.out.println("/////////////////////");
        System.out.println("История");
        Map<Integer, Node<Task>> nodes = inMemoryTaskManager.getInMemoryHistory().getNodes();
        for (Node<Task> node : nodes.values()) {
            System.out.println(node.getValue());
        }
    }
}