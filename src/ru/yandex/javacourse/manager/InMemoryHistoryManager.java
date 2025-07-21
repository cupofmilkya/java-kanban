package ru.yandex.javacourse.manager;

import ru.yandex.javacourse.collections.Node;
import ru.yandex.javacourse.tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;
    private Node<Task> tail;
    private final Map<Integer, Node<Task>> nodes = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) return;

        if (nodes.containsKey(task.getId())) {
            removeNode(nodes.get(task.getId()));
        }

        linkLast(task);
    }

    @Override
    public void remove(int id) {
        if (nodes.containsKey(id)) {
            removeNode(nodes.get(id));
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    private void removeNode(Node<Task> node) {
        if (node == null) return;

        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        } else {
            head = node.getNext();
        }

        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        } else {
            tail = node.getPrev();
        }

        nodes.remove(node.getValue().getId());
    }

    public void linkLast(Task task) {
        Node<Task> newNode = new Node<>(task, tail, null);

        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }

        nodes.put(task.getId(), newNode);
    }

    public Task getTask(int id) {
        if (nodes.containsKey(id)) {
            return nodes.get(id).getValue();
        }
        return null;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node<Task> current = head;

        while (current != null) {
            tasks.add(current.getValue());
            current = current.getNext();
        }

        return tasks;
    }

    public boolean contains(Task task) {
        return task != null && nodes.containsKey(task.getId());
    }

    public int size() {
        return nodes.size();
    }
}