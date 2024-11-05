package ua.yarynych.taskapi.service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ua.yarynych.taskapi.entity.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for consuming tasks from Kafka.
 * This class listens to the "task_created" topic and maintains a list of consumed tasks.
 * An optional class for checking the logic of sending messages to the Kafka topic
 */
@Service
public class KafkaConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final List<Task> tasks = new ArrayList<>();

    /**
     * Consumes a task from the Kafka topic "task_created".
     *
     * @param task the task object consumed from the Kafka topic.
     */
    @KafkaListener(topics = "task_created", groupId = "task_group")
    public void consumeTask(Task task) {
        tasks.add(task);
        logger.info("Consumed task with ID: {}", task.getId());
    }

    /**
     * Retrieves the list of consumed tasks.
     *
     * @return a list of consumed tasks.
     */
    public List<Task> getTasks() {
        logger.info("Retrieving consumed tasks. Total tasks: {}", tasks.size());
        return new ArrayList<>(tasks);
    }
}
