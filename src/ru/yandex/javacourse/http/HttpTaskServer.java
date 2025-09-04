package ru.yandex.javacourse.http;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.javacourse.http.handlers.tasksHandler;
import ru.yandex.javacourse.manager.Managers;
import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.Task;

import java.net.InetSocketAddress;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();

        manager.addTask(new Task("Task", "Description", Status.NEW,
                Duration.ofMinutes(45), LocalDateTime.now()));
        manager.addTask(new Task("Task 2", "Description 2", Status.IN_PROGRESS,
                Duration.ofMinutes(25), LocalDateTime.now().plus(Duration.ofMinutes(50))));
        manager.addTask(new Task("Task 3", "Description 3", Status.IN_PROGRESS,
                Duration.ofMinutes(25), LocalDateTime.now().plus(Duration.ofMinutes(150))));

        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);

        httpServer.createContext("/tasks", new tasksHandler(manager));
        httpServer.start();
        System.out.println("Сервер запущен: http://localhost:8080/tasks");
    }
}
