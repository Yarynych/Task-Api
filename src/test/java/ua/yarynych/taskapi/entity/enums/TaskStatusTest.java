package ua.yarynych.taskapi.entity.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskStatusTest {

    @Test
    void testGetValue() {
        assertEquals("Pending", TaskStatus.PENDING.getValue());
        assertEquals("In Progress", TaskStatus.IN_PROGRESS.getValue());
        assertEquals("Completed", TaskStatus.COMPLETED.getValue());
        assertEquals("Cancelled", TaskStatus.CANCELLED.getValue());
    }

    @Test
    void testFromValue_ValidValues() {
        assertEquals(TaskStatus.PENDING, TaskStatus.fromValue("Pending"));
        assertEquals(TaskStatus.IN_PROGRESS, TaskStatus.fromValue("In Progress"));
        assertEquals(TaskStatus.COMPLETED, TaskStatus.fromValue("Completed"));
        assertEquals(TaskStatus.CANCELLED, TaskStatus.fromValue("Cancelled"));
    }

    @Test
    void testFromValue_ValidValues_CaseInsensitive() {
        assertEquals(TaskStatus.PENDING, TaskStatus.fromValue("pending"));
        assertEquals(TaskStatus.IN_PROGRESS, TaskStatus.fromValue("in progress"));
        assertEquals(TaskStatus.COMPLETED, TaskStatus.fromValue("completed"));
        assertEquals(TaskStatus.CANCELLED, TaskStatus.fromValue("cancelled"));
    }

    @Test
    void testFromValue_InvalidValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            TaskStatus.fromValue("Non-existent Status");
        });

        assertEquals("Invalid status value: Non-existent Status", exception.getMessage());
    }
}
