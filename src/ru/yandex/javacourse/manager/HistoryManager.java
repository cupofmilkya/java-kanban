package ru.yandex.javacourse.manager;

import ru.yandex.javacourse.collections.LinkedListOfTasks;
import ru.yandex.javacourse.tasks.Task;

public interface HistoryManager {
    void add(Task task);

    LinkedListOfTasks getHistory();

    void remove(int id);
}
