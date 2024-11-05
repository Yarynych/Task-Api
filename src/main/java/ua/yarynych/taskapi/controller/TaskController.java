package ua.yarynych.taskapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.yarynych.taskapi.entity.Task;
import ua.yarynych.taskapi.entity.dto.TaskDto;
import ua.yarynych.taskapi.service.TaskService;

import java.util.List;

/**
 * Controller class for managing tasks. This class handles incoming HTTP requests related to tasks
 * and communicates with the TaskService to perform operations.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Creates a new task.
     *
     * @param taskDto the DTO containing the details of the task to create.
     * @return a ResponseEntity containing the ID of the created task.
     */
    @Operation(summary = "Create a new task", description = "Creates a new task with the provided details.")
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto) {
        logger.info("Received request to create task: {}", taskDto);
        Long taskId = taskService.createTask(taskDto);
        logger.info("Task created with ID: {}", taskId);
        return ResponseEntity.ok("Task created with ID: " + taskId);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete.
     * @return a ResponseEntity indicating the result of the deletion.
     */
    @Operation(summary = "Delete a task", description = "Deletes a task by its ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@Parameter(description = "ID of the task to delete") @PathVariable Long id) {
        logger.info("Received request to delete task with ID: {}", id);
        taskService.deleteTask(id);
        logger.info("Task with ID: {} deleted successfully.", id);
        return ResponseEntity.ok("Task deleted successfully.");
    }

    /**
     * Updates the status of a task.
     *
     * @param id     the ID of the task to update.
     * @param status the new status of the task.
     * @return a ResponseEntity indicating the result of the update.
     */
    @Operation(summary = "Update task status", description = "Updates the status of a task.")
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateTaskStatus(@Parameter(description = "ID of the task to update") @PathVariable Long id,
                                              @Parameter(description = "New status for the task") @RequestParam String status) {
        logger.info("Received request to update status of task with ID: {} to {}", id, status);
        taskService.updateTaskStatus(id, status);
        logger.info("Status of task with ID: {} updated successfully to {}", id, status);
        return ResponseEntity.ok("Task status updated successfully.");
    }

    /**
     * Updates fields of a task.
     *
     * @param id      the ID of the task to update.
     * @param taskDto the DTO containing the new task details.
     * @return a ResponseEntity indicating the result of the update.
     */
    @Operation(summary = "Update task fields", description = "Updates the fields of a task.")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateTaskFields(@Parameter(description = "ID of the task to update") @PathVariable Long id,
                                              @RequestBody TaskDto taskDto) {
        logger.info("Received request to update fields of task with ID: {}", id);
        taskService.updateTaskFields(id, taskDto);
        logger.info("Fields of task with ID: {} updated successfully.", id);
        return ResponseEntity.ok("Task fields updated successfully.");
    }

    /**
     * Retrieves all tasks.
     *
     * @return a list of all tasks.
     */
    @Operation(summary = "Get all tasks", description = "Retrieves a list of all tasks.")
    @GetMapping("/all")
    public List<Task> getAllTasks() {
        logger.info("Received request to retrieve all tasks.");
        List<Task> tasks = taskService.getAllTasks();
        logger.info("Retrieved {} tasks.", tasks.size());
        return tasks;
    }
}