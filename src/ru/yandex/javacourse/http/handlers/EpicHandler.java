package ru.yandex.javacourse.http.handlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.exceptions.manager.TimeOverlapConflictException;
import ru.yandex.javacourse.http.adapters.LocalDateTimeAdapter;
import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.http.adapters.DurationAdapter;
import ru.yandex.javacourse.tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public EpicHandler(TaskManager manager) {
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
                    if (paths[1].equals("epics") && paths.length == 2) {
                        Epic[] tasks = manager.getEpics().toArray(new Epic[0]);

                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(Duration.class, new DurationAdapter())
                                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                                .create();
                        String json = gson.toJson(tasks);

                        super.sendText(httpExchange, json);
                    } else if (paths[1].equals("epics") && paths.length == 3) {
                        try {
                            int id = Integer.parseInt(paths[2]);
                            Epic task = (Epic) manager.getTask(id);

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
                    } else if (paths[1].equals("epics") && paths.length == 4 && paths[3].equals("subtasks")) {
                        try {
                            int id = Integer.parseInt(paths[2]);

                            Subtask[] tasks = manager.getEpicSubtasks(id).toArray(new Subtask[0]);

                            Gson gson = new GsonBuilder()
                                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                                    .create();
                            String json = gson.toJson(tasks);

                            super.sendText(httpExchange, json);
                        } catch (NumberFormatException e) {
                            httpExchange.sendResponseHeaders(400, -1);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(400, -1);
                    }
                }
                case "POST" -> {
                    if (paths[1].equals("epics") && paths.length == 2) {
                        try (InputStream is = httpExchange.getRequestBody()) {
                            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                            Gson gson = new GsonBuilder()
                                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                                    .serializeNulls()
                                    .create();

                            JsonObject obj = JsonParser.parseString(body).getAsJsonObject();

                            String title = obj.get("title").getAsString();
                            String description = obj.get("description").getAsString();

                            Epic epic = new Epic(title, description, manager);

                            if (obj.has("id") && !obj.get("id").isJsonNull()) {
                                int id = obj.get("id").getAsInt();
                                epic.setId(id);
                            }

                            if (obj.has("subtasksIDs") && obj.get("subtasksIDs").isJsonArray()) {
                                JsonArray subtasks = obj.getAsJsonArray("subtasksIDs");
                                for (JsonElement el : subtasks) {
                                    epic.addSubtask(el.getAsInt());
                                }
                            }

                            try {
                                if (epic.getId() == null) {
                                    manager.addTask(epic);
                                    httpExchange.sendResponseHeaders(201, -1);
                                } else {
                                    if (manager.getTask(epic.getId()) == null) {
                                        httpExchange.sendResponseHeaders(404, -1);
                                    } else {
                                        manager.updateTask(epic);
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
                    if (paths[1].equals("epics") && paths.length == 3) {
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
