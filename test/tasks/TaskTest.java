package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.manager.Managers;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TaskTest {
    private InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void createInMemoryTaskManager() {
        inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
    }

    @Test
    @DisplayName("Проверка на равенство задач типа Task при одинаковом Id")
    public void equalsTasksWithSameIdTest() {
        Task task = new Task("Task", "Task description", Status.NEW);
        inMemoryTaskManager.addTask(task);

        Task task2 = new Task("Task", "Task description", Status.NEW);
        inMemoryTaskManager.addTask(task2);

        assertEquals(task, inMemoryTaskManager.getTask(task.getId()));
    }

    @Test
    @DisplayName("Проверка на неравенство задач типа Task при разном Id")
    public void notEqualsTasksWithSameIdTest() {
        Task task = new Task("Task", "Task description", Status.NEW);
        inMemoryTaskManager.addTask(task);

        Task task2 = new Task("Task", "Task description", Status.NEW);
        inMemoryTaskManager.addTask(task2);

        assertNotEquals(task, inMemoryTaskManager.getTask(task2.getId()));
    }
}
