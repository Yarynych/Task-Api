package ua.yarynych.taskapi.entity.errors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomExceptionsTest {

    @Test
    void testInvalidTaskStatusException() {
        String message = "Invalid task status provided.";
        InvalidTaskStatusException exception = new InvalidTaskStatusException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testTaskAlreadyExistsException() {
        String message = "Task with this name already exists.";
        TaskAlreadyExistsException exception = new TaskAlreadyExistsException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testTaskLimitReachedException() {
        String message = "Task limit reached.";
        TaskLimitReachedException exception = new TaskLimitReachedException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testTaskNotFoundException() {
        String message = "Task not found.";
        TaskNotFoundException exception = new TaskNotFoundException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }
}
