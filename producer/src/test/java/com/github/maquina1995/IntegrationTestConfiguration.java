package com.github.maquina1995;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Configuration
public class IntegrationTestConfiguration {

//	private static DockerImageName ZOOKEEPER_IMAGE = DockerImageName.parse("confluentinc/cp-zookeeper:latest");

	private KafkaContainer container;

	@Bean(initMethod = "start", destroyMethod = "close")
	public KafkaContainer kafka() {
		DockerImageName kafkaImage = DockerImageName.parse("confluentinc/cp-kafka:latest");
		container = new KafkaContainer(kafkaImage).withEmbeddedZookeeper();
		return container;
	}

	@Bean
	public Map<String, Object> producerProps(KafkaContainer kafkaContainer) {
		Map<String, Object> props = new ConcurrentHashMap<>();
		log.info("Kafka hashCode {}", kafkaContainer.hashCode());
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put("listeners", "PLAINTEXT://localhost:9092");
		props.put("advertised.listeners", "PLAINTEXT://localhost:9092");

		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		return props;
	}

	@Bean
	public Map<String, Object> consumerProps(KafkaContainer kafkaContainer) {
		Map<String, Object> props = new ConcurrentHashMap<>();
		log.info("Kafka hashCode {}", kafkaContainer.hashCode());
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		return props;
	}
}
