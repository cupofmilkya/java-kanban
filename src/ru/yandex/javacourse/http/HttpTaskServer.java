package ru.yandex.javacourse.http;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.javacourse.http.handlers.SubtaskHandler;
import ru.yandex.javacourse.http.handlers.TasksHandler;
import ru.yandex.javacourse.manager.Managers;
import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.Subtask;
import ru.yandex.javacourse.tasks.Task;

import java.net.InetSocketAddress;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HttpTaskServer {
    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();

        manager.addTask(new Task("Task", "Description", Status.NEW,
                Duration.ofMinutes(45), LocalDateTime.now()));
        manager.addTask(new Epic("Task 2", "Description 2",
                new ArrayList<Integer>(), manager));
        manager.addTask(new Subtask("Subtask 3", "Description 3",
                Status.IN_PROGRESS, 1, Duration.ofMinutes(25), LocalDateTime.now().plusMinutes(150)));

        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);

        httpServer.createContext("/tasks", new TasksHandler(manager));
        httpServer.createContext("/subtasks", new SubtaskHandler(manager));
        httpServer.start();
        System.out.println("Сервер запущен: http://localhost:8080/tasks");
    }
}
