package ua.yarynych.taskapi.entity.errors;

/**
 * Exception thrown when an invalid task status is encountered.
 */
public class InvalidTaskStatusException extends RuntimeException {
    public InvalidTaskStatusException(String message) {
        super(message);
    }
}
