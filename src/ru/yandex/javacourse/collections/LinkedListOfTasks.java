package ru.yandex.javacourse.collections;

import ru.yandex.javacourse.tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class LinkedListOfTasks {
    private Node<Task> head;
    private Node<Task> tail;

    private final Map<Integer, Node<Task>> nodes;

    public LinkedListOfTasks() {
        nodes = new HashMap<>();
    }

    public boolean add(Task t) {
        if (contains(t) || t == null) {
            return false;
        }

        if (head == null) {
            tail = head = new Node<Task>(t, null, null);
            nodes.put(t.getId(), head);
            return true;
        }

        Node<Task> common = new Node<Task>(t, tail, null);
        tail.setNext(common);
        tail = common;

        nodes.put(t.getId(), common);
        return true;
    }

    public void remove(int id) {
        if (!nodes.containsKey(id)) {
            return;
        }

        Node<Task> nodeToRemove = nodes.get(id);

        if (nodeToRemove.getPrev() != null) {
            nodeToRemove.getPrev().setNext(nodeToRemove.getNext());
        } else {
            head = nodeToRemove.getNext();
        }

        if (nodeToRemove.getNext() != null) {
            nodeToRemove.getNext().setPrev(nodeToRemove.getPrev());
        } else {
            tail = nodeToRemove.getPrev();
        }

        nodes.remove(id);
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public int size() {
        return nodes.size();
    }

    public boolean contains(Object o) {
        if (!(o instanceof Task)) {
            return false;
        }

        Task t = (Task) o;
        return nodes.containsKey(t.getId());
    }

    public Task getTask(int id) {
        if (!nodes.containsKey(id)) {
            return null;
        }
        return nodes.get(id).getValue();
    }

    public Map<Integer, Node<Task>> getNodes() {
        return nodes;
    }
}