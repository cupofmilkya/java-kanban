package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.javacourse.manager.FileBackedTaskManager;
import ru.yandex.javacourse.manager.Managers;
import ru.yandex.javacourse.tasks.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager fileBackedTaskManager;
    private Path tempFile;

    @BeforeEach
    public void createInMemoryTaskManager() {
        try {
            tempFile = Files.createTempFile("temp", ".csv");
            tempFile.toFile().deleteOnExit();
            String content = "0,TASK,Задача 1,NEW,Вот такая задача из файла\n" +
                    "1,EPIC,Задача 2,DONE,Вот такая вторая задача из файла\n" +
                    "2,SUBTASK,Задача 3,DONE,Вот такая третья задача из файла,1\n";
            Files.write(tempFile, content.getBytes());
        } catch (IOException e) {
            System.out.println("Ошибка при создания временного файла для тестов: " + e.getMessage());
        }

        fileBackedTaskManager = (FileBackedTaskManager) Managers.getDefaultFileManager();
    }

    @Test
    @DisplayName("Проверка на возможность загрузки файла в FileBackedTaskManager")
    public void thatFileBackedTaskManagerTest_CorrectlyLoadDifferentTaskTypes() {
        fileBackedTaskManager.loadFromFile(tempFile.toFile());

        assertEquals(1, fileBackedTaskManager.getTasks().size());
        assertEquals(1, fileBackedTaskManager.getEpics().size());
        assertEquals(1, fileBackedTaskManager.getSubtasks().size());
    }

    @Test
    @DisplayName("Проверка на возможность сохранения файла в FileBackedTaskManager")
    public void thatFileBackedTaskManagerTest_CorrectlySaveDifferentTaskTypes() {
        fileBackedTaskManager.loadFromFile(tempFile.toFile());
        fileBackedTaskManager.setFilePath(tempFile.toFile().getAbsolutePath());

        Task task = new Task("Задача для сохранения", "Описание новой задачи", Status.NEW);
        fileBackedTaskManager.addTask(task);

        FileBackedTaskManager newFileBackedTaskManager = (FileBackedTaskManager) Managers.getDefaultFileManager();
        newFileBackedTaskManager.loadFromFile(tempFile.toFile());

        assertEquals(2, newFileBackedTaskManager.getTasks().size());
    }

    @Test
    @DisplayName("Проверка на корректное сохранение данных и их состояний в файле в FileBackedTaskManager")
    public void thatFileBackedTaskManagerTest_CorrectlyContainsDifferentTaskTypes() {
        fileBackedTaskManager.loadFromFile(tempFile.toFile());
        fileBackedTaskManager.setFilePath(tempFile.toFile().getAbsolutePath());

        Task task = new Task("Задача для сохранения", "Описание новой задачи", Status.NEW);
        fileBackedTaskManager.addTask(task);

        FileBackedTaskManager newFileBackedTaskManager = (FileBackedTaskManager) Managers.getDefaultFileManager();
        newFileBackedTaskManager.loadFromFile(tempFile.toFile());

        assertEquals(task.getStatus(), newFileBackedTaskManager.getTask(task.getId()).getStatus());
        assertEquals(task.getTitle(), newFileBackedTaskManager.getTask(task.getId()).getTitle());
        assertEquals(task.getDescription(), newFileBackedTaskManager.getTask(task.getId()).getDescription());
    }

    @Test
    @DisplayName("Проверка на корректное поведение программы при неправильном вводе из файла в FileBackedTaskManager")
    public void thatFileBackedTaskManagerTest_CorrectlyWorkWithBrokenFiles() {
        try {
            Path tempFile1 = Files.createTempFile("temp1", ".csv");
            tempFile1.toFile().deleteOnExit();
            String content = "0,TA  SK,,,Задача 1,NEW,Вот такая задача из файла\n" +
                    "1,EPIC,Задача 2,DONE,Вот такая вторая задача из файла\n" +
                    "2,SUBTASK,Задача 3,DONE,Вот такая третья задача из файла,1\n";
            Files.write(tempFile1, content.getBytes());
            fileBackedTaskManager.loadFromFile(tempFile1.toFile());
        } catch (IOException e) {
            System.out.println("Ошибка при создания временного файла для тестов: " + e.getMessage());
        }

        assertEquals(2, fileBackedTaskManager.getAllTasks().size());
    }

    @Test
    @DisplayName("Проверка на поведение FileBackedTaskManager при загрузке пустого файла")
    public void thatFileBackedTaskManagerTest_CorrectlyWorkWithEmptyFiles() {
        try {
            Path tempFile1 = Files.createTempFile("temp1", ".csv");
            tempFile1.toFile().deleteOnExit();
            String content = "";
            Files.write(tempFile1, content.getBytes());
            fileBackedTaskManager.loadFromFile(tempFile1.toFile());
        } catch (IOException e) {
            System.out.println("Ошибка при создания временного файла для тестов: " + e.getMessage());
        }

        assertEquals(0, fileBackedTaskManager.getAllTasks().size());
    }
}