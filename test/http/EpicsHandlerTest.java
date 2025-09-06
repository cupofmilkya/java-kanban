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

public class EpicsHandlerTest {
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
    @DisplayName("Проверка на получение эпика через HTTP")
    public void getEpicHttpTest() throws IOException, InterruptedException {
        Task task = new Task("Task", "Description", Status.NEW,
                Duration.ofMinutes(45), LocalDateTime.now());
        manager.addTask(task);
        Epic epic = new Epic("Epic", "Description 2", manager);
        manager.addTask(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 3",
                Status.IN_PROGRESS, epic.getId(), Duration.ofMinutes(25), LocalDateTime.now().plusMinutes(150));
        manager.addTask(subtask);


        String name = manager.getTask(epic.getId()).getTitle();
        String description = manager.getTask(epic.getId()).getDescription();
        Status status = manager.getTask(epic.getId()).getStatus();
        Duration duration = manager.getTask(epic.getId()).getDuration();
        LocalDateTime date = manager.getTask(epic.getId()).getStartTime();

        URI uri = URI.create("http://localhost:8080/epics");
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

        Epic[] tasks = gson.fromJson(body, Epic[].class);
        Epic taskJson = tasks[0];
        taskJson.setTaskManager(manager);

        assertEquals(name, taskJson.getTitle());
        assertEquals(description, taskJson.getDescription());
        assertEquals(status, taskJson.getStatus());
        assertEquals(duration, taskJson.getDuration());
        assertEquals(date, taskJson.getStartTime());
    }

    @Test
    @DisplayName("Проверка на получение эпика по id через HTTP")
    public void getEpicIdHttpTest() throws IOException, InterruptedException {
        Task task = new Task("Task", "Description", Status.NEW,
                Duration.ofMinutes(45), LocalDateTime.now());
        manager.addTask(task);
        Epic epic = new Epic("Epic", "Description 2", manager);
        manager.addTask(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 3",
                Status.IN_PROGRESS, epic.getId(), Duration.ofMinutes(25), LocalDateTime.now().plusMinutes(150));
        manager.addTask(subtask);


        String name = manager.getTask(epic.getId()).getTitle();
        String description = manager.getTask(epic.getId()).getDescription();
        Status status = manager.getTask(epic.getId()).getStatus();
        Duration duration = manager.getTask(epic.getId()).getDuration();
        LocalDateTime date = manager.getTask(epic.getId()).getStartTime();

        URI uri = URI.create("http://localhost:8080/epics/" + epic.getId());
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

        Epic taskJson = gson.fromJson(body, Epic.class);
        taskJson.setTaskManager(manager);


        assertEquals(name, taskJson.getTitle());
        assertEquals(description, taskJson.getDescription());
        assertEquals(status, taskJson.getStatus());
        assertEquals(duration, taskJson.getDuration());
        assertEquals(date, taskJson.getStartTime());
    }

    @Test
    @DisplayName("Проверка на получение эпика по id через HTTP")
    public void getEpicIdSubtasksHttpTest() throws IOException, InterruptedException {
        Task task = new Task("Task", "Description", Status.NEW,
                Duration.ofMinutes(45), LocalDateTime.now());
        manager.addTask(task);
        Epic epic = new Epic("Epic", "Description 2", manager);
        manager.addTask(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 3",
                Status.IN_PROGRESS, epic.getId(), Duration.ofMinutes(25), LocalDateTime.now().plusMinutes(150));
        manager.addTask(subtask);


        String name = manager.getTask(subtask.getId()).getTitle();
        String description = manager.getTask(subtask.getId()).getDescription();
        Status status = manager.getTask(subtask.getId()).getStatus();
        Duration duration = manager.getTask(subtask.getId()).getDuration();
        LocalDateTime date = manager.getTask(subtask.getId()).getStartTime();

        URI uri = URI.create("http://localhost:8080/epics/" + epic.getId() + "/subtasks");
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

        Subtask[] subtasksJson = gson.fromJson(body, Subtask[].class);
        Subtask subtaskJson = subtasksJson[0];

        assertEquals(name, subtaskJson.getTitle());
        assertEquals(description, subtaskJson.getDescription());
        assertEquals(status, subtaskJson.getStatus());
        assertEquals(duration, subtaskJson.getDuration());
        assertEquals(date, subtaskJson.getStartTime());
    }

    @Test
    @DisplayName("Проверка на удаление эпика через HTTP")
    public void deleteEpicHttpTest() throws IOException, InterruptedException {
        Task task = new Task("Task", "Description", Status.NEW,
                Duration.ofMinutes(45), LocalDateTime.now());
        manager.addTask(task);
        Epic epic = new Epic("Epic", "Description 2", manager);
        manager.addTask(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 3",
                Status.IN_PROGRESS, epic.getId(), Duration.ofMinutes(25), LocalDateTime.now().plusMinutes(150));
        manager.addTask(subtask);


        URI uri = URI.create("http://localhost:8080/epics/" + task.getId());
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        HttpRequest request = requestBuilder
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "text/html")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getTasks().size());
    }
}