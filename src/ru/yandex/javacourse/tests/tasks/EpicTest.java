package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.manager.Managers;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.Subtask;
import ru.yandex.javacourse.tasks.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EpicTest {
    private InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void createInMemoryTaskManager() {
        inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
    }

    @Test
    @DisplayName("Проверка работы получения статуса Эпика при обновлении статусов его сабтасков")
    public void EpicUpdateStatusTest() {
        Epic epic = new Epic("Epic1", "Epic description", new ArrayList<Integer>(), inMemoryTaskManager);
        Subtask subtask = new Subtask("Subtask", "Subtask description", Status.NEW, epic.getId());
        Subtask subtask1 = new Subtask("Subtask", "Subtask description", Status.NEW, epic.getId());
        epic.addSubtask(subtask.getId());
        epic.addSubtask(subtask1.getId());

        inMemoryTaskManager.addTask(epic);
        inMemoryTaskManager.addTask(subtask);
        inMemoryTaskManager.addTask(subtask1);

        assertEquals(Status.NEW, epic.getStatus());

        subtask.updateStatus(Status.IN_PROGRESS);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());

        subtask.updateStatus(Status.DONE);
        subtask1.updateStatus(Status.DONE);

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    @DisplayName("Проверка работы получения статуса Эпика при обновлении статусов его сабтасков")
    public void EpicAddSubtaskTest() {
        Epic epic = new Epic("Epic", "Epic description", new ArrayList<Integer>(), inMemoryTaskManager);
        Subtask subtask = new Subtask("Subtask", "Subtask description", Status.NEW, epic.getId());
        epic.addSubtask(subtask.getId());

        assertEquals(subtask.getId(), epic.getSubtasksIDs().get(0));
    }

    @Test
    @DisplayName("Проверка на невозможность добавления Эпика себя в свои Сабтаски")
    public void inabilityToAddAnEpicToOneselfTest() {
        Epic epic = new Epic("Epic", "Epic description", new ArrayList<Integer>(), inMemoryTaskManager);
        inMemoryTaskManager.addTask(epic);
        epic.addSubtask(epic.getId());

        ArrayList<Integer> expected = new ArrayList<Integer>();

        assertEquals(expected, epic.getSubtasksIDs());
    }

    @Test
    @DisplayName("Проверка на равенство задач типов Epic при одинаковом Id")
    public void equalsOfEpicWithTheSameIdTest() {
        Epic epic = new Epic("Epic", "Epic description", new ArrayList<Integer>(), inMemoryTaskManager);
        inMemoryTaskManager.addTask(epic);

        assertEquals(epic, inMemoryTaskManager.getTask(epic.getId()));
    }

    @Test
    @DisplayName("Проверка на неравенство задач типов Epic при разном Id")
    public void notEqualsOfEpicWithTheSameIdTest() {
        Epic epic = new Epic("Epic", "Epic description", new ArrayList<Integer>(), inMemoryTaskManager);
        Epic epic1 = new Epic("Epic", "Epic description", new ArrayList<Integer>(), inMemoryTaskManager);
        inMemoryTaskManager.addTask(epic);

        assertNotEquals(epic, epic1);
    }
}
