package ua.yarynych.taskapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import ua.yarynych.taskapi.entity.Task;
import ua.yarynych.taskapi.entity.dto.TaskDto;
import ua.yarynych.taskapi.service.TaskService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTaskSuccess() {
        // Arrange
        TaskDto taskDto = new TaskDto();
        taskDto.setName("New Task");
        taskDto.setDescription("Description");
        taskDto.setStatus("Pending");

        Long createdTaskId = 1L;
        when(taskService.createTask(any(TaskDto.class))).thenReturn(createdTaskId);

        // Act
        ResponseEntity<?> response = taskController.createTask(taskDto);

        // Assert
        assertEquals(ResponseEntity.ok("Task created with ID: " + createdTaskId), response);
        verify(taskService).createTask(taskDto);
    }

    @Test
    void testDeleteTaskSuccess() {
        // Arrange
        Long taskId = 1L;

        // Act
        ResponseEntity<?> response = taskController.deleteTask(taskId);

        // Assert
        assertEquals(ResponseEntity.ok("Task deleted successfully."), response);
        verify(taskService).deleteTask(taskId);
    }

    @Test
    void testUpdateTaskStatusSuccess() {
        // Arrange
        Long taskId = 1L;
        String newStatus = "Completed";

        // Act
        ResponseEntity<?> response = taskController.updateTaskStatus(taskId, newStatus);

        // Assert
        assertEquals(ResponseEntity.ok("Task status updated successfully."), response);
        verify(taskService).updateTaskStatus(taskId, newStatus);
    }

    @Test
    void testUpdateTaskFieldsSuccess() {
        // Arrange
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();
        taskDto.setName("Updated Task");
        taskDto.setDescription("Updated Description");
        taskDto.setStatus("In Progress");

        // Act
        ResponseEntity<?> response = taskController.updateTaskFields(taskId, taskDto);

        // Assert
        assertEquals(ResponseEntity.ok("Task fields updated successfully."), response);
        verify(taskService).updateTaskFields(taskId, taskDto);
    }

    @Test
    void testGetAllTasksSuccess() {
        // Arrange
        Task task1 = new Task();
        Task task2 = new Task();
        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskService.getAllTasks()).thenReturn(tasks);

        // Act
        List<Task> response = taskController.getAllTasks();

        // Assert
        assertEquals(tasks, response);
        verify(taskService).getAllTasks();
    }

    @Test
    void testGetAllTasksEmpty() {
        // Arrange
        when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

        // Act
        List<Task> response = taskController.getAllTasks();

        // Assert
        assertTrue(response.isEmpty());
        verify(taskService).getAllTasks();
    }
}
