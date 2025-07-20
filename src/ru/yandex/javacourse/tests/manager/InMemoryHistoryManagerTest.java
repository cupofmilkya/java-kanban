package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.yandex.javacourse.collections.LinkedListOfTasks;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.manager.Managers;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class InMemoryHistoryManagerTest {
    private InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void createInMemoryTaskManager() {
        inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
    }

    @Test
    @DisplayName("Проверка на неналичие повторных просмотров в истории InMemoryHistoryManager")
    public void taskMustNotBeSaved_moreThanOnce_Test() {
        Task task = new Task("Task", "Task description", Status.NEW);
        inMemoryTaskManager.addTask(task);

        inMemoryTaskManager.getTask(task.getId());
        inMemoryTaskManager.getTask(task.getId());
        inMemoryTaskManager.getTask(task.getId());

        LinkedListOfTasks tasks = inMemoryTaskManager.getInMemoryHistory();

        assertEquals(1, tasks.size());
    }
}
