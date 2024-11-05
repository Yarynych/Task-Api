package ua.yarynych.taskapi.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Enumeration representing the possible statuses of a Task.
 */
@AllArgsConstructor
@Getter
public enum TaskStatus {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String value;

    /**
     * Gets the TaskStatus from a string value.
     *
     * @param value the string value representing the status.
     * @return the corresponding TaskStatus.
     */
    public static TaskStatus fromValue(String value) {
        for (TaskStatus status : TaskStatus.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status value: " + value);
    }
}
