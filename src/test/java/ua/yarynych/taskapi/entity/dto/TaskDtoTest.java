package ua.yarynych.taskapi.entity.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskDtoTest {

    @Test
    public void testTaskDtoGettersAndSetters() {
        TaskDto taskDto = new TaskDto();

        taskDto.setName("Test Task");
        taskDto.setDescription("This is a test task.");
        taskDto.setStatus("PENDING");

        assertThat(taskDto.getName()).isEqualTo("Test Task");
        assertThat(taskDto.getDescription()).isEqualTo("This is a test task.");
        assertThat(taskDto.getStatus()).isEqualTo("PENDING");
    }

    @Test
    public void testTaskDtoNoArgsConstructor() {
        TaskDto taskDto = new TaskDto();
        assertThat(taskDto).isNotNull();
    }

    @Test
    public void testTaskDtoAllArgsConstructor() {
        TaskDto taskDto = new TaskDto();
        taskDto.setName("Task 1");
        taskDto.setDescription("Task 1 Description");
        taskDto.setStatus("COMPLETED");

        assertThat(taskDto.getName()).isEqualTo("Task 1");
        assertThat(taskDto.getDescription()).isEqualTo("Task 1 Description");
        assertThat(taskDto.getStatus()).isEqualTo("COMPLETED");
    }
}