package ru.yandex.javacourse.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.exceptions.manager.TimeOverlapConflictException;
import ru.yandex.javacourse.http.adapters.LocalDateTimeAdapter;
import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.tasks.Subtask;
import ru.yandex.javacourse.tasks.Task;
import ru.yandex.javacourse.http.adapters.DurationAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public SubtaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        URI uri = httpExchange.getRequestURI();
        String path = uri.getPath();

        String[] paths = path.split("/");

        try {
            switch (method) {
                case "GET" -> {
                    if (paths[1].equals("subtasks") && paths.length == 2) {
                        Subtask[] tasks = manager.getSubtasks().toArray(new Subtask[0]);

                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(Duration.class, new DurationAdapter())
                                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                                .create();
                        String json = gson.toJson(tasks);

                        super.sendText(httpExchange, json);
                    } else if (paths[1].equals("subtasks") && paths.length == 3) {
                        try {
                            int id = Integer.parseInt(paths[2]);
                            Subtask task = (Subtask) manager.getTask(id); //todo Мэйби поменять на гетСабтаск

                            if (task == null) {
                                super.sendNotFound(httpExchange);
                                return;
                            }

                            Gson gson = new GsonBuilder()
                                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                                    .create();
                            String json = gson.toJson(task);

                            super.sendText(httpExchange, json);
                        } catch (NumberFormatException e) {
                            httpExchange.sendResponseHeaders(400, -1);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(400, -1);
                    }
                }
                case "POST" -> {
                    if (paths[1].equals("subtasks") && paths.length == 2) {
                        try (InputStream is = httpExchange.getRequestBody()) {
                            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                            Gson gson = new GsonBuilder()
                                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                                    .serializeNulls()
                                    .create();

                            Subtask task = gson.fromJson(body, Subtask.class);

                            try {
                                if (task.getId() == null) {
                                    manager.addTask(task);
                                    httpExchange.sendResponseHeaders(201, -1);
                                } else {
                                    if (manager.getTask(task.getId()) == null) {
                                        httpExchange.sendResponseHeaders(404, -1);
                                    } else {
                                        manager.updateTask(task);
                                        httpExchange.sendResponseHeaders(200, -1);
                                    }
                                }
                            } catch (TimeOverlapConflictException e) {
                                super.sendHasOverlaps(httpExchange);
                            }
                        } catch (Exception e) {
                            httpExchange.sendResponseHeaders(500, -1);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(400, -1);
                    }
                }
                case "DELETE" -> {
                    if (paths[1].equals("subtasks") && paths.length == 3) {
                        try {
                            int id = Integer.parseInt(paths[2]);

                            if (manager.getTask(id) == null) {
                                super.sendNotFound(httpExchange);
                                return;
                            }

                            manager.removeTask(id);

                            String json = "Задача с id " + id + " успешно удалена.";

                            super.sendText(httpExchange, json);
                        } catch (NumberFormatException e) {
                            httpExchange.sendResponseHeaders(400, -1);
                        }
                    }
                }
                default -> {
                    httpExchange.sendResponseHeaders(405, -1);
                }
            }
        } catch (NumberFormatException e) {
            super.sendNotFound(httpExchange);
        } catch (Exception e) {

            httpExchange.sendResponseHeaders(500, -1);
        }
    }
}
