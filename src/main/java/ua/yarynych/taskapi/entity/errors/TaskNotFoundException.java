package ua.yarynych.taskapi.entity.errors;

/**
 * Exception thrown when a task cannot be found by its ID.
 */
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
