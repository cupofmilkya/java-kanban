package ru.yandex.javacourse.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.exceptions.manager.NotFoundException;
import ru.yandex.javacourse.exceptions.manager.TimeOverlapConflictException;
import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public TasksHandler(TaskManager manager) {
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
                    if (paths[1].equals("tasks") && paths.length == 2) {
                        Task[] tasks = manager.getTasks().toArray(new Task[0]);

                        String json = getGson().toJson(tasks);

                        super.sendText(httpExchange, json);
                    } else if (paths[1].equals("tasks") && paths.length == 3) {
                        try {
                            int id = Integer.parseInt(paths[2]);
                            Task task = manager.getTask(id);

                            String json = getGson().toJson(task);

                            super.sendText(httpExchange, json);
                        } catch (NumberFormatException e) {
                            httpExchange.sendResponseHeaders(400, -1);
                        } catch (NotFoundException e) {
                            super.sendNotFound(httpExchange);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(400, -1);
                    }
                }
                case "POST" -> {
                    if (paths[1].equals("tasks") && paths.length == 2) {
                        try (InputStream is = httpExchange.getRequestBody()) {
                            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                            Task task = getGson().fromJson(body, Task.class);

                            try {
                                if (task.getId() == null) {
                                    manager.addTask(task);
                                    httpExchange.sendResponseHeaders(201, -1);
                                } else {
                                    manager.updateTask(task);
                                    httpExchange.sendResponseHeaders(200, -1);
                                }
                            } catch (TimeOverlapConflictException e) {
                                super.sendHasOverlaps(httpExchange);
                            } catch (NotFoundException e) {
                                super.sendNotFound(httpExchange);
                            }
                        } catch (Exception e) {
                            httpExchange.sendResponseHeaders(500, -1);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(400, -1);
                    }
                }
                case "DELETE" -> {
                    if (paths[1].equals("tasks") && paths.length == 3) {
                        try {
                            int id = Integer.parseInt(paths[2]);

                            manager.removeTask(id);

                            String json = "Задача с id " + id + " успешно удалена.";

                            super.sendText(httpExchange, json);
                        } catch (NumberFormatException e) {
                            httpExchange.sendResponseHeaders(400, -1);
                        } catch (NotFoundException e) {
                            super.sendNotFound(httpExchange);
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
