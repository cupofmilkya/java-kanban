package http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.*;
import org.junit.jupiter.api.*;

import ru.yandex.javacourse.http.HttpTaskServer;
import ru.yandex.javacourse.http.adapters.DurationAdapter;
import ru.yandex.javacourse.http.adapters.LocalDateTimeAdapter;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.manager.Managers;
import ru.yandex.javacourse.tasks.Epic;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.Subtask;
import ru.yandex.javacourse.tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class HistoryHandlerTest {
    static InMemoryTaskManager manager = (InMemoryTaskManager) Managers.getDefault();

    @BeforeAll
    public static void initialize() throws IOException {
        HttpTaskServer.startServer(manager);
    }

    @AfterEach
    public void removeTasks() {
        manager.removeAllTasks();
    }

    @AfterAll
    public static void closing() {
        HttpTaskServer.stopServer();
    }

    @Test
    @DisplayName("Проверка на получение задачи через HTTP")
    public void getHistoryHttpTest() throws IOException, InterruptedException {
        Task task = new Task("Task", "Description", Status.NEW,
                Duration.ofMinutes(45), LocalDateTime.now());
        manager.addTask(task);
        Epic epic = new Epic("Epic", "Description 2", manager);
        manager.addTask(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 3",
                Status.IN_PROGRESS, epic.getId(), Duration.ofMinutes(25), LocalDateTime.now().plusMinutes(150));
        manager.addTask(subtask);

        manager.getTask(task.getId());

        URI uri = URI.create("http://localhost:8080/history");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        HttpRequest request = requestBuilder
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());

        String body = response.body();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        Task[] tasks = gson.fromJson(body, Task[].class);

        assertEquals(1, tasks.length);
    }
}