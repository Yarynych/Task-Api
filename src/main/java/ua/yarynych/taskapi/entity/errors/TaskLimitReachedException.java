package ua.yarynych.taskapi.entity.errors;

/**
 * Exception thrown when the task limit is reached.
 */
public class TaskLimitReachedException extends RuntimeException {
    public TaskLimitReachedException(String message) {
        super(message);
    }
}
