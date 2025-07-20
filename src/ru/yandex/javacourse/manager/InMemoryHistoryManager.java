package ru.yandex.javacourse.manager;

import ru.yandex.javacourse.collections.LinkedListOfTasks;
import ru.yandex.javacourse.tasks.Task;

public class InMemoryHistoryManager implements HistoryManager {

    private final LinkedListOfTasks history;

    public InMemoryHistoryManager() {
        history = new LinkedListOfTasks();
    }

    @Override
    public LinkedListOfTasks getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {

        if (!history.add(task)) {
            history.remove(task.getId());
        }

        history.add(task);
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }

}