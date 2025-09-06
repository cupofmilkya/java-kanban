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

public class SubtasksHandlerTest {
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
    @DisplayName("Проверка на получение подзадачи через HTTP")
    public void getSubtaskHttpTest() throws IOException, InterruptedException {
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

        URI uri = URI.create("http://localhost:8080/subtasks");
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

        Subtask[] tasks = gson.fromJson(body, Subtask[].class);
        Subtask taskJson = tasks[0];

        assertEquals(name, taskJson.getTitle());
        assertEquals(description, taskJson.getDescription());
        assertEquals(status, taskJson.getStatus());
        assertEquals(duration, taskJson.getDuration());
        assertEquals(date, taskJson.getStartTime());
    }

    @Test
    @DisplayName("Проверка на получение подзадачи по id через HTTP")
    public void getSubtaskIdHttpTest() throws IOException, InterruptedException {
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

        URI uri = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
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

        Subtask taskJson = gson.fromJson(body, Subtask.class);

        assertEquals(name, taskJson.getTitle());
        assertEquals(description, taskJson.getDescription());
        assertEquals(status, taskJson.getStatus());
        assertEquals(duration, taskJson.getDuration());
        assertEquals(date, taskJson.getStartTime());
    }

    @Test
    @DisplayName("Проверка на создание подзадачи через HTTP")
    public void postSubtaskHttpTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "Description 2", manager);
        manager.addTask(epic);

        String name = "Title";
        String description = "Description";
        Status status = Status.NEW;
        int id = epic.getId();
        Duration duration = Duration.ofMinutes(25);
        LocalDateTime date = LocalDateTime.now().plusMinutes(150);

        Subtask subtask = new Subtask(name, description, status, id, duration, date);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        String json = gson.toJson(subtask);
        json = json.replace("\"id\":" + subtask.getId(), "\"id\":null");

        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);
        assertEquals(201, response.statusCode(), "Размер: " + manager.getTasks().size() +
                ", " + json + "\n" + manager.getAllTasks());

        Subtask taskToCheck = manager.getSubtasks().getFirst();

        assertEquals(name, taskToCheck.getTitle());
        assertEquals(description,  taskToCheck.getDescription());
        assertEquals(status,  taskToCheck.getStatus());
        assertEquals(duration,  taskToCheck.getDuration());
        assertEquals(date,  taskToCheck.getStartTime());
    }

    @Test
    @DisplayName("Проверка на удаление подзадачи через HTTP")
    public void deleteSubtaskHttpTest() throws IOException, InterruptedException {
        Task task = new Task("Task", "Description", Status.NEW,
                Duration.ofMinutes(45), LocalDateTime.now());
        manager.addTask(task);
        Epic epic = new Epic("Epic", "Description 2", manager);
        manager.addTask(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 3",
                Status.IN_PROGRESS, epic.getId(), Duration.ofMinutes(25), LocalDateTime.now().plusMinutes(150));
        manager.addTask(subtask);


        URI uri = URI.create("http://localhost:8080/tasks/" + task.getId());
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