package ru.yandex.javacourse.tasks;

import java.util.ArrayList;

public class TaskConvertor {
    public static Task fromString(String line) {
        String[] parts = line.split(",");

        try {
            if (parts.length < 5) {
                System.out.println("Ошибка в строке: не хватает данных. Строка: " + String.join(",", parts));
                return null;
            }

            switch (parts[1]) {
                case "TASK" -> {
                    return new Task(parts[2], parts[4], Status.valueOf(parts[3]), Integer.parseInt(parts[0]));
                }
                case "EPIC" -> {
                    return new Epic(parts[2], parts[4], new ArrayList<Integer>(), Integer.parseInt(parts[0]));
                }
                case "SUBTASK" -> {
                    if (parts.length < 6) {
                        System.out.println("Ошибка в подзадаче: не указан ID эпика. Строка: " +
                                String.join(",", parts));
                        return null;
                    }
                    return new Subtask(parts[2], parts[4], Status.valueOf(parts[3]), Integer.parseInt(parts[5]),
                            Integer.parseInt(parts[0]));
                }
                default -> {
                    System.out.println("Неизвестный тип задачи: " + parts[1] + ". Строка: " +
                            String.join(",", parts));
                    return null;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка формата числа в строке: " + String.join(",", parts));
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка значения статуса в строке: " + String.join(",", parts));
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка при конвертации строки: " + String.join(",", parts));
        }

        return null;
    }
}
