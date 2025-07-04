package tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.Subtask;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubtaskTest {
    @Test
    @DisplayName("Проверка на невозможность добавления Сабтаска себя в качестве своего Эпика")
    public void inabilityToAddSubtaskAsEpicToOneselfTest() {
        Subtask subtask = new Subtask("Subtask", "Subtask description", Status.NEW);
        subtask.setEpicID(subtask.getId());

        int expected = -1;

        assertEquals(expected, subtask.getEpicID());
    }
}
