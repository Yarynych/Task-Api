package ua.yarynych.taskapi.entity.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) representing a Task with its details.
 * This class is used to transfer data between server and clients.
 */
@Data
public class TaskDto {
    private String name;
    private String description;
    private String status;
}
