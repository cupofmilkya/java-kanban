package ru.yandex.javacourse.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacourse.http.adapters.LocalDateTimeAdapter;
import ru.yandex.javacourse.manager.HistoryManager;
import ru.yandex.javacourse.manager.TaskManager;
import ru.yandex.javacourse.tasks.Task;
import ru.yandex.javacourse.http.adapters.DurationAdapter;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public HistoryHandler(TaskManager manager) {
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
                    if (paths[1].equals("history") && paths.length == 2) {
                        HistoryManager hm = manager.getInMemoryHistory();
                        Task[] tasks = hm.getHistory().toArray(new Task[0]);

                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(Duration.class, new DurationAdapter())
                                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                                .create();
                        String json = gson.toJson(tasks);

                        super.sendText(httpExchange, json);
                    } else {
                        httpExchange.sendResponseHeaders(400, -1);
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