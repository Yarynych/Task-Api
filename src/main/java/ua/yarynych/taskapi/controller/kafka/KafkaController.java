package ua.yarynych.taskapi.controller.kafka;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.yarynych.taskapi.entity.Task;
import ua.yarynych.taskapi.service.kafka.KafkaConsumerService;

import java.util.List;

/**
 * REST controller for managing Kafka tasks.
 * This controller exposes endpoints to interact with Kafka consumer services.
 */
@RestController
@RequestMapping("/api/kafka")
@Tag(name = "Kafka Controller", description = "Controller for handling Kafka operations")
public class KafkaController {

    private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);

    private final KafkaConsumerService kafkaConsumerService;

    @Autowired
    public KafkaController(KafkaConsumerService kafkaConsumerService) {
        this.kafkaConsumerService = kafkaConsumerService;
    }

    /**
     * Retrieves the list of tasks consumed from Kafka.
     *
     * @return a ResponseEntity containing a list of tasks.
     */
    @GetMapping("/tasks")
    @Operation(summary = "Get consumed tasks", description = "Retrieves all tasks that have been consumed from Kafka")
    public ResponseEntity<List<Task>> getTasksFromKafka() {
        logger.info("Fetching tasks from Kafka.");

        List<Task> tasks = kafkaConsumerService.getTasks();
        logger.info("Retrieved {} tasks from Kafka.", tasks.size());

        return ResponseEntity.ok(tasks);
    }
}
