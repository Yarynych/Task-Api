package ua.yarynych.taskapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import ua.yarynych.taskapi.entity.Task;
import ua.yarynych.taskapi.entity.dto.TaskDto;
import ua.yarynych.taskapi.entity.enums.TaskStatus;
import ua.yarynych.taskapi.entity.errors.InvalidTaskStatusException;
import ua.yarynych.taskapi.entity.errors.TaskAlreadyExistsException;
import ua.yarynych.taskapi.entity.errors.TaskLimitReachedException;
import ua.yarynych.taskapi.entity.errors.TaskNotFoundException;
import ua.yarynych.taskapi.repository.TaskRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private KafkaTemplate<String, Task> kafkaTemplate;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteTask() {
        // Arrange
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(true);

        // Act
        taskService.deleteTask(taskId);

        // Assert
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void testDeleteTaskNotFound() {
        // Arrange
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(false);

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(taskId));
        verify(taskRepository, never()).deleteById(taskId);
    }

    @Test
    void testUpdateTaskStatus() {
        // Arrange
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        taskService.updateTaskStatus(taskId, "Completed");

        // Assert
        assertEquals(TaskStatus.COMPLETED, task.getStatus());
        verify(taskRepository).save(task);
    }

    @Test
    void testUpdateTaskStatusNotFound() {
        // Arrange
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskStatus(taskId, "COMPLETED"));
    }

    @Test
    void testUpdateTaskFieldsNotFound() {
        // Arrange
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();
        taskDto.setName("Updated Task");
        taskDto.setDescription("Updated Description");
        taskDto.setStatus("Completed");

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskFields(taskId, taskDto));
    }

    @Test
    void testGetAllTasks() {
        // Arrange
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findAll()).thenReturn(Collections.singletonList(task));
        when(taskRepository.count()).thenReturn(1L);

        // Act
        var tasks = taskService.getAllTasks();

        // Assert
        assertNotNull(tasks);
        assertEquals(1, tasks.size());
    }

    @Test
    void testGetAllTasksEmpty() {
        // Arrange
        when(taskRepository.count()).thenReturn(0L);

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.getAllTasks());
    }

    @Test
    void testCreateTaskWithExistingName() {
        // Arrange
        TaskDto taskDto = new TaskDto();
        taskDto.setName("Task 1"); // Існуюча назва
        taskDto.setDescription("Description 1");
        taskDto.setStatus("Pending");

        when(taskRepository.existsByName(taskDto.getName())).thenReturn(true);

        // Act & Assert
        assertThrows(TaskAlreadyExistsException.class, () -> taskService.createTask(taskDto));
    }

    @Test
    void testCreateTaskWithEmptyName() {
        // Arrange
        TaskDto taskDto = new TaskDto();
        taskDto.setName(""); // Пусте ім'я
        taskDto.setDescription("Description 1");
        taskDto.setStatus("Pending");

        // Act & Assert
        assertThrows(InvalidTaskStatusException.class, () -> taskService.createTask(taskDto));
    }

    @Test
    void testUpdateTaskStatusInvalid() {
        // Arrange
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(new Task()));

        // Act & Assert
        assertThrows(InvalidTaskStatusException.class, () -> taskService.updateTaskStatus(taskId, ""));
    }

    @Test
    void testCreateTaskLimitReached() {
        // Arrange
        TaskDto taskDto = new TaskDto();
        taskDto.setName("Task 1");
        taskDto.setDescription("Description 1");
        taskDto.setStatus("Pending");

        when(taskRepository.existsByName(taskDto.getName())).thenReturn(false);
        when(taskRepository.count()).thenReturn(100L); // Ліміт досягнуто

        // Act & Assert
        assertThrows(TaskLimitReachedException.class, () -> taskService.createTask(taskDto));
    }

    @Test
    void testUpdateTaskFieldsInvalidStatus() {
        // Arrange
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();
        taskDto.setName("Updated Task");
        taskDto.setDescription("Updated Description");
        taskDto.setStatus(""); // Невірний статус

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(new Task()));

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskFields(taskId, taskDto));
    }
}
