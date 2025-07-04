package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.manager.Managers;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class InMemoryHistoryManagerTest {
    private InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void createInMemoryTaskManager() {
        inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
    }

    @Test
    @DisplayName("Проверка на сохранение конкретного состояния объекта в InMemoryHistoryManager")
    public void tasksAddedToHistoryManagerSavePreviousVersionTest() {
        Task task = new Task("Task", "Task description", Status.NEW);
        inMemoryTaskManager.addTask(task);

        inMemoryTaskManager.getTask(task.getId());
        task.updateStatus(Status.DONE);
        ArrayList<Task> tasks = inMemoryTaskManager.getInMemoryHistory();

        assertNotEquals(task.getStatus(), tasks.getFirst().getStatus());
    }
}
