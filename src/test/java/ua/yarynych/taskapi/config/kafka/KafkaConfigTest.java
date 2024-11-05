package ua.yarynych.taskapi.config.kafka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ua.yarynych.taskapi.entity.Task;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KafkaConfigTest {

    @InjectMocks
    private KafkaConfig kafkaConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testKafkaTemplate() {
        // Act
        KafkaTemplate<String, Task> kafkaTemplate = kafkaConfig.kafkaTemplate();

        // Assert
        assertNotNull(kafkaTemplate);
    }

    @Test
    void testProducerFactory() {
        // Act
        ProducerFactory<String, Task> producerFactory = kafkaConfig.producerFactory();

        // Assert
        assertNotNull(producerFactory);
        assertTrue(producerFactory instanceof DefaultKafkaProducerFactory, "ProducerFactory should be an instance of DefaultKafkaProducerFactory");
    }

    @Test
    void testConsumerFactory() {
        // Act
        ConsumerFactory<String, Task> consumerFactory = kafkaConfig.consumerFactory();

        // Assert
        assertNotNull(consumerFactory);
        assertTrue(consumerFactory instanceof DefaultKafkaConsumerFactory, "ConsumerFactory should be an instance of DefaultKafkaConsumerFactory");
    }

    @Test
    void testKafkaListenerContainerFactory() {
        // Act
        ConcurrentKafkaListenerContainerFactory<String, Task> factory = kafkaConfig.kafkaListenerContainerFactory();

        // Assert
        assertNotNull(factory);
        assertNotNull(factory.getConsumerFactory(), "The consumer factory in the container factory should not be null.");
    }
}
