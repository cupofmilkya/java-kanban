package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.yandex.javacourse.manager.InMemoryHistoryManager;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.manager.Managers;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InMemoryHistoryManagerTest {
    private InMemoryTaskManager inMemoryTaskManager;
    private InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeEach
    public void createInMemoryTaskManager() {
        inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        inMemoryHistoryManager = (InMemoryHistoryManager) inMemoryTaskManager.getInMemoryHistory();
    }

    @Test
    @DisplayName("Проверка на неналичие повторных просмотров в истории InMemoryHistoryManager")
    public void taskMustNotBeSaved_moreThanOnce_Test() {
        Task task = new Task("Task", "Task description", Status.NEW);
        inMemoryTaskManager.addTask(task);

        inMemoryTaskManager.getTask(task.getId());
        inMemoryTaskManager.getTask(task.getId());
        inMemoryTaskManager.getTask(task.getId());

        InMemoryHistoryManager tasks = (InMemoryHistoryManager) inMemoryTaskManager.getInMemoryHistory();

        assertEquals(1, tasks.getTasks().size());
    }

    @Test
    @DisplayName("Проверка корректной работы структуры данных")
    public void correctWorkLinkedListOfTasksTest() {
        Task task1 = new Task("Task2", "Task description", Status.NEW);
        Task task2 = new Task("Task2", "Task description", Status.NEW);
        Task task3 = new Task("Task3", "Task description", Status.NEW);

        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.linkLast(task3);

        assertEquals(inMemoryHistoryManager.getTask(task1.getId()), task1);
        assertEquals(inMemoryHistoryManager.size(), 3);
    }

    @Test
    @DisplayName("Проверка корректной работы структуры данных")
    public void correctWorkOfRemove_LinkedListOfTasksTest() {
        Task task1 = new Task("Task2", "Task description", Status.NEW);

        inMemoryHistoryManager.add(task1);

        inMemoryHistoryManager.remove(task1.getId());

        assertNull(inMemoryHistoryManager.getTask(task1.getId()));
    }
}