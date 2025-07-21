package ru.yandex.javacourse.collections;

import ru.yandex.javacourse.tasks.Task;

import java.util.ArrayList;
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
        if (nodes.containsKey(t.getId()) || t == null) {
            return false;
        }

        Task commonTask = new Task(t.getTitle(), t.getDescription(), t.getStatus(), t.getId());

        if (head == null) {
            tail = head = new Node<Task>(t, null, null);
            nodes.put(commonTask.getId(), head);
            return true;
        }

        linkLast(commonTask);
        return true;
    }

    public void linkLast(Task t) {
        Task commonTask = new Task(t.getTitle(), t.getDescription(), t.getStatus(), t.getId());

        Node<Task> common = new Node<Task>(commonTask, tail, null);
        tail.setNext(common);
        tail = common;

        nodes.put(commonTask.getId(), common);
    }

    public void removeNode(Node<Task> node) {
        if (!nodes.containsValue(node)) {
            return;
        }

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

        nodes.remove(node);
    }

    public Task getTask(int id) {
        if (!nodes.containsKey(id)) {
            return null;
        }
        return nodes.get(id).getValue();
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
}