package ru.yandex.javacourse.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.http.adapters.LocalDateTimeAdapter;
import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.tasks.Task;
import ru.yandex.javacourse.http.adapters.DurationAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class tasksHandler implements HttpHandler {
    private final TaskManager manager;

    public tasksHandler(TaskManager manager) {
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
                    if (paths[1].equals("tasks") && paths.length > 1) {
                        Task[] tasks = manager.getTasks().toArray(new Task[0]);

                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(Duration.class, new DurationAdapter())
                                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                                .create();
                        String json = gson.toJson(tasks);

                        byte[] response = json.getBytes(StandardCharsets.UTF_8);
                        httpExchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                        httpExchange.sendResponseHeaders(200, response.length);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response);
                        }
                    } else {
                        httpExchange.sendResponseHeaders(400, -1);
                    }
                }
                default -> {
                    httpExchange.sendResponseHeaders(405, -1);
                }
            }
        } catch (NumberFormatException e) {
            httpExchange.sendResponseHeaders(404, -1);
        } catch (Exception e) {

            httpExchange.sendResponseHeaders(500, -1);
        }
    }
}
