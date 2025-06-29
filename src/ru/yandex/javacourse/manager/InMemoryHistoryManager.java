package ru.yandex.javacourse.manager;

import ru.yandex.javacourse.tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> history;
    private final static int HISTORY_SIZE = 10;

    public InMemoryHistoryManager() {
        history = new ArrayList<>();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        if (history.size() >= HISTORY_SIZE) {
            history.removeFirst();
        }
        history.add(task);
    }
}
