package manager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import ru.yandex.javacourse.manager.Managers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagersTest {
    @Test
    @DisplayName("Проверка на возвращаемый стандартный тип")
    public void ManagersGetDefaultNotNullTest() {
        assertNotNull(Managers.getDefault());
        assertNotNull(Managers.getDefaultHistory());
    }
}
