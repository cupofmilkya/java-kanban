package collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.collections.LinkedListOfTasks;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.Task;

import static org.junit.jupiter.api.Assertions.*;


public class LinkedListOfTasksTest {
    private LinkedListOfTasks linkedListOfTasks;

    @BeforeEach
    public void createLinkedListOfTasks() {
        linkedListOfTasks = new LinkedListOfTasks();
    }

    @Test
    @DisplayName("Проверка корректной работы структуры данных")
    public void correctWorkLinkedListOfTasksTest() {
        Task task1 = new Task("Task2", "Task description", Status.NEW);
        Task task2 = new Task("Task2", "Task description", Status.NEW);
        Task task3 = new Task("Task3", "Task description", Status.NEW);

        linkedListOfTasks.add(task1);
        linkedListOfTasks.add(task2);
        linkedListOfTasks.add(task3);

        assertEquals(linkedListOfTasks.getTask(task1.getId()), task1);
        assertEquals(linkedListOfTasks.size(), 3);
    }

    @Test
    @DisplayName("Проверка корректной работы структуры данных")
    public void correctWorkOfRemove_LinkedListOfTasksTest() {
        Task task1 = new Task("Task2", "Task description", Status.NEW);

        linkedListOfTasks.add(task1);

        linkedListOfTasks.remove(task1.getId());

        assertNull(linkedListOfTasks.getTask(task1.getId()));
    }
}
