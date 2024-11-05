package ua.yarynych.taskapi.service.kafka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.yarynych.taskapi.entity.Task;
import ua.yarynych.taskapi.service.kafka.KafkaConsumerService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig
@EmbeddedKafka(partitions = 1, topics = { "task_created" })
class KafkaConsumerServiceTest {

    private KafkaConsumerService kafkaConsumerService;
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @BeforeEach
    void setUp() {
        kafkaConsumerService = new KafkaConsumerService();
    }

    @Test
    void testConsumeTask() {
        // Arrange
        Task task = new Task();
        task.setId(1L); // Set a test ID for the task

        // Act
        kafkaConsumerService.consumeTask(task);

        // Assert
        List<Task> tasks = kafkaConsumerService.getTasks();
        assertNotNull(tasks);
        assertEquals(1, tasks.size(), "There should be one task in the list.");
        assertEquals(task.getId(), tasks.get(0).getId(), "The task ID should match the consumed task.");
    }

    @Test
    void testGetTasks() {
        // Arrange
        Task task1 = new Task();
        task1.setId(1L);
        Task task2 = new Task();
        task2.setId(2L);

        // Act
        kafkaConsumerService.consumeTask(task1);
        kafkaConsumerService.consumeTask(task2);

        // Assert
        List<Task> tasks = kafkaConsumerService.getTasks();
        assertNotNull(tasks);
        assertEquals(2, tasks.size(), "There should be two tasks in the list.");
        assertEquals(task1.getId(), tasks.get(0).getId(), "The first task ID should match the first consumed task.");
        assertEquals(task2.getId(), tasks.get(1).getId(), "The second task ID should match the second consumed task.");
    }
}
