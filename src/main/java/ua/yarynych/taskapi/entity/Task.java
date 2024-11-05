package ua.yarynych.taskapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.yarynych.taskapi.entity.enums.TaskStatus;

import java.time.LocalDateTime;

/**
 * Entity representing a Task in the system.
 * This class is mapped to the "tasks" table in the database.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tasks")
public class Task {

    /**
     * The unique identifier of the task.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the task.
     */
    private String name;

    /**
     * The description of the task.
     */
    private String description;

    /**
     * The status of the task, represented by an enumeration.
     */
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    /**
     * The date and time when the task was created.
     */
    private LocalDateTime created_date = LocalDateTime.now();


    /**
     * Sets the status of the task based on a string value.
     *
     * @param status the status as a string.
     */
    public void setStatus(String status) {
        this.status = TaskStatus.fromValue(status);
    }


    /**
     * Gets the status of the task as a string.
     *
     * @return the status as a string.
     */
    public String getStatusAsString() {
        return status != null ? status.getValue() : null;
    }
}
