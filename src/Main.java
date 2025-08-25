import ru.yandex.javacourse.manager.*;
import ru.yandex.javacourse.tasks.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("tempMain", ".csv");
            tempFile.toFile().deleteOnExit();
            String content = "0,TASK,Задача 1,NEW,Вот такая задача из файла,2025-06-25T14:30:45.123456789,PT2H30M\n" +
                    "1,EPIC,Задача 2,DONE,Вот такая вторая задача из файла,2025-06-25T14:30:45.123456789,PT2H30M\n" +
                    "2,SUBTASK,Задача 3,DONE,Вот такая третья задача из файла,1,2025-06-25T17:30:00.123456789,PT2H30M\n" +
                    "3,SUBTASK,Задача 4,DONE,Вот такая третья задача из файла,1,2025-05-25T17:30:00.123456789,PT2H30M\n" +
                    "4,TASK,Задача 1,NEW,Задача не приоритет,0001-01-01T00:00,PT2H30M\n";
            Files.write(tempFile, content.getBytes());
        } catch (IOException e) {
            System.out.println("Ошибка при создания временного файла для тестов: " + e.getMessage());
        }

        if (tempFile == null) {
            System.out.println("Ошибка при создании временного файла");
            return;
        }

        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile.toFile());

        System.out.println("Задачи:");
        for (Task taskDefault : fileBackedTaskManager.getTasks()) {
            System.out.println(taskDefault);
        }
        System.out.println("Эпики:");
        for (Epic taskEpic : fileBackedTaskManager.getEpics()) {
            System.out.println(taskEpic);

            System.out.println("  Подзадачи Эпика \"" + taskEpic.getTitle() + "\":");
            for (Subtask task : fileBackedTaskManager.getEpicSubtasks(taskEpic.getId())) {
                System.out.println("  --> " + task);
            }
        }

        System.out.println("Подзадачи:");
        for (Task subtask : fileBackedTaskManager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("Приоритеты:");
        for (Task task : fileBackedTaskManager.getPrioritizedTasks()) {
            System.out.println(task);
        }
    }
}