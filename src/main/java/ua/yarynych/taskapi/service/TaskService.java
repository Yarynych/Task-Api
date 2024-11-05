package ua.yarynych.taskapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ua.yarynych.taskapi.entity.Task;
import ua.yarynych.taskapi.entity.dto.TaskDto;
import ua.yarynych.taskapi.entity.enums.TaskStatus;
import ua.yarynych.taskapi.entity.errors.InvalidTaskStatusException;
import ua.yarynych.taskapi.entity.errors.TaskAlreadyExistsException;
import ua.yarynych.taskapi.entity.errors.TaskLimitReachedException;
import ua.yarynych.taskapi.entity.errors.TaskNotFoundException;
import ua.yarynych.taskapi.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for managing tasks. This class handles the business logic related to tasks,
 * including creating, updating, and deleting tasks, as well as interacting with Kafka.
 */
@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final KafkaTemplate<String, Task> kafkaTemplate;

    @Autowired
    public TaskService(TaskRepository taskRepository, KafkaTemplate<String, Task> kafkaTemplate) {
        this.taskRepository = taskRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Creates a new task based on the provided TaskDto.
     *
     * @param taskDto the DTO containing task details.
     * @return the ID of the created task.
     */
    public Long createTask(TaskDto taskDto) {
        validateCreateTask(taskDto);

        logger.info("Creating task with name: {}", taskDto.getName());

        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setStatus(TaskStatus.PENDING.getValue());
        task.setCreated_date(LocalDateTime.now());

        taskRepository.save(task);
        kafkaTemplate.send("task_created", task);

        logger.info("Task created with ID: {}", task.getId());
        return task.getId();
    }


    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete.
     */
    public void deleteTask(Long id) {
        validateTaskExists(id);
        logger.info("Deleting task with ID: {}", id);
        taskRepository.deleteById(id);
    }


    /**
     * Updates the status of a task.
     *
     * @param id the ID of the task to update.
     * @param status the new status of the task.
     */
    public void updateTaskStatus(Long id, String status) {
        logger.info("Updating status of task with ID: {} to {}", id, status);

        Task task = taskRepository.findById(id).orElseThrow(() -> {
            logger.error("Task with ID '{}' not found.", id);
            return new TaskNotFoundException("Task not found");
        });

        validateStatus(status);

        task.setStatus(TaskStatus.fromValue(status).getValue());
        taskRepository.save(task);

        logger.info("Status of task with ID: {} updated to {}", id, status);
    }


    /**
     * Updates the fields of a task.
     *
     * @param id the ID of the task to update.
     * @param taskDto the DTO containing the new task details.
     */
    public void updateTaskFields(Long id, TaskDto taskDto) {
        validateTaskExists(id);
        validateStatus(taskDto.getStatus());

        logger.info("Updating fields of task with ID: {}", id);

        Task task = taskRepository.findById(id).orElseThrow(() -> {
            logger.error("Task with ID '{}' not found.", id);
            return new TaskNotFoundException("Task not found");
        });

        if (taskDto.getName() != null) {
            task.setName(taskDto.getName());
            logger.info("Task name updated to '{}'", taskDto.getName());
        }

        if (taskDto.getDescription() != null) {
            task.setDescription(taskDto.getDescription());
            logger.info("Task description updated.");
        }

        if (taskDto.getStatus() != null) {
            task.setStatus(TaskStatus.fromValue(taskDto.getStatus()).getValue());
            logger.info("Task status updated to '{}'", taskDto.getStatus());
        }

        taskRepository.save(task);
        logger.info("Task with ID: {} updated successfully.", id);
    }


    /**
     * Retrieves all tasks from the repository.
     *
     * @return a list of all tasks.
     */
    public List<Task> getAllTasks() {
        if (taskRepository.count() == 0) {
            logger.warn("No tasks found in the repository.");
            throw new TaskNotFoundException("No tasks available.");
        }
        logger.info("Retrieving all tasks.");
        return taskRepository.findAll();
    }


    private void validateCreateTask(TaskDto taskDto) {
        if (taskRepository.existsByName(taskDto.getName())) {
            logger.error("Task with name '{}' already exists.", taskDto.getName());
            throw new TaskAlreadyExistsException("Task with this name already exists.");
        }

        if (taskRepository.count() >= 100) {
            logger.error("Task limit reached. Current count: {}", taskRepository.count());
            throw new TaskLimitReachedException("Task limit reached.");
        }

        if (taskDto.getName() == null || taskDto.getName().isEmpty()) {
            logger.error("Task name is invalid.");
            throw new InvalidTaskStatusException("Task name cannot be null or empty.");
        }

        if (taskDto.getDescription() == null || taskDto.getDescription().isEmpty()) {
            logger.error("Task description is invalid.");
            throw new InvalidTaskStatusException("Task description cannot be null or empty.");
        }

        if (taskDto.getStatus() == null || taskDto.getStatus().isEmpty()) {
            logger.error("Task status is invalid.");
            throw new InvalidTaskStatusException("Task status cannot be null or empty.");
        }
    }

    private void validateTaskExists(Long id) {
        if (!taskRepository.existsById(id)) {
            logger.error("Task with ID '{}' not found.", id);
            throw new TaskNotFoundException("Task not found");
        }
    }

    private void validateStatus(String status) {
        if (status == null || status.isEmpty()) {
            logger.error("Invalid task status.");
            throw new InvalidTaskStatusException("Task status cannot be null or empty.");
        }
    }
}