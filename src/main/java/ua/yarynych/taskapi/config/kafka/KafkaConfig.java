package ua.yarynych.taskapi.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ua.yarynych.taskapi.entity.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka configuration class that sets up the Kafka producer and consumer
 * with the necessary settings and serializers.
 */
@Configuration
public class KafkaConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

    private final String kafkaServer="localhost:9092";


    /**
     * Creates a KafkaTemplate bean for sending messages to Kafka.
     *
     * @return the KafkaTemplate instance.
     */
    @Bean
    public KafkaTemplate<String, Task> kafkaTemplate() {
        logger.info("Creating KafkaTemplate for sending messages.");
        return new KafkaTemplate<>(producerFactory());
    }


    /**
     * Configures the producer factory with necessary properties.
     *
     * @return the configured ProducerFactory instance.
     */
    ProducerFactory<String, Task> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        logger.info("Configuring ProducerFactory with Kafka server: {}", kafkaServer);
        return new DefaultKafkaProducerFactory<>(configProps);
    }


    /**
     * Creates the consumer factory with properties for consuming messages from Kafka.
     *
     * @return the configured ConsumerFactory instance.
     */
    @Bean
    public ConsumerFactory<String, Task> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "task_group");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Task.class.getName());

        logger.info("Configuring ConsumerFactory with Kafka server: {}", kafkaServer);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }


    /**
     * Creates a ConcurrentKafkaListenerContainerFactory for processing Kafka messages.
     *
     * @return the configured ConcurrentKafkaListenerContainerFactory instance.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Task> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Task> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        logger.info("Creating ConcurrentKafkaListenerContainerFactory for consuming messages.");
        return factory;
    }
}
