package ru.yandex.javacourse.manager;

import ru.yandex.javacourse.exceptions.manager.ManagerSaveException;
import ru.yandex.javacourse.tasks.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager {
    String filePath;

    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
    }

    public FileBackedTaskManager(HistoryManager historyManager, String filePath) {
        super(historyManager);
        this.filePath = filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        try {
            save();
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении в файл: " + e.getMessage());
        }
    }

    @Override
    public void removeTask(int taskToRemove) {
        super.removeTask(taskToRemove);
        try {
            save();
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении в файл: " + e.getMessage());
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        try {
            save();
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении в файл: " + e.getMessage());
        }
    }

    @Override
    public void addSubtasks(ArrayList<Subtask> tasks) {
        super.addSubtasks(tasks);
        try {
            save();
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении в файл: " + e.getMessage());
        }
    }

    private void save() throws ManagerSaveException {
        if (filePath == null) {
            System.out.println("Сохранение невозможно, нет пути для файла");
        }
        try (FileWriter writer = new FileWriter(filePath)) {
            List<Task> tasksToWrite = new ArrayList<Task>(getTasks());

            for (Task task : tasksToWrite) {
                writer.write(task.toString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
            throw new ManagerSaveException(e.getMessage());
        }
    }

    private void load(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    Task task = TaskConvertor.fromString(line);

                    if (task == null) {
                        System.out.println("Ошибка: задача не была создана из строки: " + line);
                        continue;
                    }

                    if (task instanceof Epic) {
                        ((Epic) task).setTaskManager(this);
                    }

                    super.addTask(task);
                } catch (Exception e) {
                    System.out.println("Ошибка при считывании строки файла: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }
    }

    public void loadFromFile(File file) {
        load(file.getAbsolutePath());
    }
}
