package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.exceptions.manager.NotFoundException;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.manager.Managers;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    private InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void createInMemoryTaskManager() {
        inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
    }

    @Test
    @DisplayName("Проверка на равенство задач типа Task при одинаковом Id")
    public void equalsTasksWithSameIdTest() throws NotFoundException {
        Task task = new Task("Task", "Task description", Status.NEW);
        inMemoryTaskManager.addTask(task);

        Task task2 = new Task("Task", "Task description", Status.NEW);
        inMemoryTaskManager.addTask(task2);

        assertEquals(task, inMemoryTaskManager.getTask(task.getId()));
    }

    @Test
    @DisplayName("Проверка на неравенство задач типа Task при разном Id")
    public void notEqualsTasksWithSameIdTest() throws NotFoundException {
        Task task = new Task("Task", "Task description", Status.NEW);
        inMemoryTaskManager.addTask(task);

        Task task2 = new Task("Task", "Task description", Status.NEW);
        inMemoryTaskManager.addTask(task2);

        assertNotEquals(task, inMemoryTaskManager.getTask(task2.getId()));
    }

    @Test
    @DisplayName("Проверка на корректность работы getEndTime в Task")
    public void task_CorrectlyReturnEndTimeTest() {
        LocalDateTime time = LocalDateTime.of(2025, 1, 1, 0, 0);
        Duration duration = Duration.ofMinutes(10);

        LocalDateTime time2 = time.plus(Duration.ofMinutes(30));

        Task task = new Task("Task", "Task description", Status.NEW, duration, time);
        Task task2 = new Task("Task", "Task description", Status.NEW, duration, time2);

        assertFalse(inMemoryTaskManager.isPairOfTasksOverlap(task, task2));

        Task task3 = new Task("Task", "Task description", Status.NEW, duration, time);
        Task task4 = new Task("Task", "Task description", Status.NEW, duration, time);

        assertTrue(inMemoryTaskManager.isPairOfTasksOverlap(task3, task4));
    }
}