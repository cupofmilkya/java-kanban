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

public class TasksHandlerTest {
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
                Duration.ofMinutes(45), LocalDateTime.now());
        manager.addTask(task);
        Epic epic = new Epic("Epic", "Description 2", manager);
        manager.addTask(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 3",
                Status.IN_PROGRESS, epic.getId(), Duration.ofMinutes(25), LocalDateTime.now().plusMinutes(150));
        manager.addTask(subtask);


        String name = manager.getTask(task.getId()).getTitle();
        String description = manager.getTask(task.getId()).getDescription();
        Status status = manager.getTask(task.getId()).getStatus();
        Duration duration = manager.getTask(task.getId()).getDuration();
        LocalDateTime date = manager.getTask(task.getId()).getStartTime();

        URI uri = URI.create("http://localhost:8080/tasks");
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
        Task taskJson = tasks[0];

        assertEquals(name, taskJson.getTitle());
        assertEquals(description, taskJson.getDescription());
        assertEquals(status, taskJson.getStatus());
        assertEquals(duration, taskJson.getDuration());
        assertEquals(date, taskJson.getStartTime());
    }

    @Test
    @DisplayName("Проверка на получение задачи по id через HTTP")
    public void getTaskIdHttpTest() throws IOException, InterruptedException {
        Task task = new Task("Task", "Description", Status.NEW,
                Duration.ofMinutes(45), LocalDateTime.now());
        manager.addTask(task);
        Epic epic = new Epic("Epic", "Description 2", manager);
        manager.addTask(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 3",
                Status.IN_PROGRESS, epic.getId(), Duration.ofMinutes(25), LocalDateTime.now().plusMinutes(150));
        manager.addTask(subtask);


        String name = manager.getTask(task.getId()).getTitle();
        String description = manager.getTask(task.getId()).getDescription();
        Status status = manager.getTask(task.getId()).getStatus();
        Duration duration = manager.getTask(task.getId()).getDuration();
        LocalDateTime date = manager.getTask(task.getId()).getStartTime();

        URI uri = URI.create("http://localhost:8080/tasks/" + task.getId());
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

        Task taskJson = gson.fromJson(body, Task.class);

        assertEquals(name, taskJson.getTitle());
        assertEquals(description, taskJson.getDescription());
        assertEquals(status, taskJson.getStatus());
        assertEquals(duration, taskJson.getDuration());
        assertEquals(date, taskJson.getStartTime());
    }

    @Test
    @DisplayName("Проверка на создание задачи через HTTP")
    public void postTaskHttpTest() throws IOException, InterruptedException {
        String name = "Title";
        String description = "Description";
        Status status = Status.NEW;
        Duration duration = Duration.ofMinutes(25);
        LocalDateTime date = LocalDateTime.now().plusMinutes(150);

        Task task = new Task(name, description, status, duration, date);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        String json = gson.toJson(task);
        json = json.replace("\"id\":0", "\"id\":null");

        URI uri = URI.create("http://localhost:8080/tasks");
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

        assertEquals(name, manager.getTask(1).getTitle());
        assertEquals(description,  manager.getTask(1).getDescription());
        assertEquals(status,  manager.getTask(1).getStatus());
        assertEquals(duration,  manager.getTask(1).getDuration());
        assertEquals(date,  manager.getTask(1).getStartTime());
    }

    @Test
    @DisplayName("Проверка на удаление задачи через HTTP")
    public void deleteTaskHttpTest() throws IOException, InterruptedException {
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