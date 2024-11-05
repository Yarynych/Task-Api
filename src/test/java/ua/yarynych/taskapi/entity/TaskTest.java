package ua.yarynych.taskapi.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.yarynych.taskapi.entity.enums.TaskStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setName("Test Task");
        task.setDescription("This is a test task.");
        task.setStatus("Pending");
    }

    @Test
    void testTaskCreation() {
        assertNotNull(task);
        assertEquals("Test Task", task.getName());
        assertEquals("This is a test task.", task.getDescription());
        assertEquals(TaskStatus.PENDING, task.getStatus());
        assertNotNull(task.getCreated_date());
    }

    @Test
    void testSetStatus() {
        task.setStatus("Completed");
        assertEquals(TaskStatus.COMPLETED, task.getStatus());
    }

    @Test
    void testGetStatusAsString() {
        task.setStatus("In Progress");
        assertEquals("In Progress", task.getStatusAsString());
    }

    @Test
    void testSetStatusInvalidValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> task.setStatus("Invalid Status"));

        assertEquals("Invalid status value: Invalid Status", exception.getMessage());
    }

    @Test
    void testCreatedDateIsSetAutomatically() {
        LocalDateTime createdDate = task.getCreated_date();
        assertNotNull(createdDate);
        assertEquals(LocalDateTime.now().getDayOfYear(), createdDate.getDayOfYear());
    }
}
