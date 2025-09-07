package ru.yandex.javacourse.http;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.javacourse.http.handlers.*;
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

public class HttpTaskServer {
    static HttpServer httpServer;

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();

        manager.addTask(new Task("Task", "Description", Status.NEW,
                Duration.ofMinutes(45), LocalDateTime.now()));
        manager.addTask(new Epic("Epic", "Description 2", manager));
        manager.addTask(new Subtask("Subtask 1", "Description 3",
                Status.IN_PROGRESS, 1, Duration.ofMinutes(25), LocalDateTime.now().plusMinutes(150)));
        manager.addTask(new Subtask("Subtask 2", "Description 4",
                Status.IN_PROGRESS, 1, Duration.ofMinutes(25), LocalDateTime.now().plusMinutes(350)));

        //для работы Истории
        manager.getTask(0);
        manager.getTask(2);

        startServer(manager);
    }

    public static void startServer(TaskManager manager) throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);

        httpServer.createContext("/tasks", new TasksHandler(manager));
        httpServer.createContext("/subtasks", new SubtaskHandler(manager));
        httpServer.createContext("/epics", new EpicHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
        httpServer.start();

        System.out.println("Сервер запущен: http://localhost:8080");
    }

    public static void stopServer() {
        httpServer.stop(0);
        System.out.println("Сервер остановлен");
    }
}
