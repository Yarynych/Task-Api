package ua.yarynych.taskapi.entity.errors;

/**
 * Exception thrown when attempting to create a task that already exists.
 */
public class TaskAlreadyExistsException extends RuntimeException {
    public TaskAlreadyExistsException(String message) {
        super(message);
    }
}
