package http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.*;
import org.junit.jupiter.api.*;

import ru.yandex.javacourse.http.HttpTaskServer;
import ru.yandex.javacourse.http.adapters.DurationAdapter;
import ru.yandex.javacourse.http.adapters.LocalDateTimeAdapter;
import ru.yandex.javacourse.manager.InMemoryTaskManager;
import ru.yandex.javacourse.manager.Managers;
import ru.yandex.javacourse.tasks.Status;
import ru.yandex.javacourse.tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class PrioritizedHandlerTest {
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
    public void getTaskHttpTest() throws IOException, InterruptedException {
        Task task = new Task("Task", "Description", Status.NEW,
                Duration.ofMinutes(45), LocalDateTime.now().minusDays(100));
        manager.addTask(task);
        Task task1 = new Task("Task", "Description", Status.NEW,
                Duration.ofMinutes(45), LocalDateTime.now().plusDays(100));
        manager.addTask(task1);

        URI uri = URI.create("http://localhost:8080/prioritized");
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

        assertEquals(task, tasks[0]);
    }
}