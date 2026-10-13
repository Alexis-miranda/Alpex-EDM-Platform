package com.alpex.validation_service.config;

import com.alpex.validation_service.dto.FinancialInstrumentDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, FinancialInstrumentDTO> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "alpex-validation-group");

        // New Spring architecture: Configure properties using text class paths to avoid deprecated instances
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        // Define the structural delegates safely
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, "org.springframework.kafka.support.serializer.JsonDeserializer");

        // Configure explicit payload mapping without importing the class directly
        props.put("spring.json.value.default.type", "com.alpex.validation_service.dto.FinancialInstrumentDTO");
        props.put("spring.json.trusted.packages", "*");
        props.put("spring.json.use.type.headers", false); // Eliminates package routing mismatch natively

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FinancialInstrumentDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FinancialInstrumentDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
