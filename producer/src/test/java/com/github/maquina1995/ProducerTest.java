package com.github.maquina1995;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import com.github.maquina1995.configuration.KafkaTopicConfig;
import com.github.maquina1995.constants.KafkaConstants;
import com.github.maquina1995.entity.MessageLog;
import com.github.maquina1995.producer.configuration.KafkaProducerConfig;

@EmbeddedKafka(partitions = 1, ports = 9092)
@SpringBootTest(classes = { KafkaTopicConfig.class, KafkaProducerConfig.class })
class ProducerTest {

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	@Autowired
	private KafkaTemplate<String, String> producerStringString;

	@Autowired
	private KafkaTemplate<String, MessageLog> producerStringPojo;

	@Test
	void testProducerStringString() {

		// Given
		String key = "key";
		String value = "value";

		try (Consumer<String, String> consumer = this.configureConsumerStringString()) {

			// When
			producerStringString.send(new ProducerRecord<>(KafkaConstants.KAFKA_TOPIC_STRING_STRING, key, value));

			// Then
			ConsumerRecord<String, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer,
					KafkaConstants.KAFKA_TOPIC_STRING_STRING);
			Assertions.assertNotNull(singleRecord);
			Assertions.assertEquals(singleRecord.key(), key);
			Assertions.assertEquals(singleRecord.value(), value);
		}
	}

	@Test
	void testProducerStringPojo() {

		// Given
		String key = "key";
		MessageLog messageLog = MessageLog.builder().message("mensaje").build();

		try (Consumer<String, MessageLog> consumer = this.configureConsumerStringPojo()) {

			// When
			producerStringPojo.send(new ProducerRecord<>(KafkaConstants.KAFKA_TOPIC_STRING_POJO, key, messageLog));

			// Then
			ConsumerRecord<String, MessageLog> singleRecord = KafkaTestUtils.getSingleRecord(consumer,
					KafkaConstants.KAFKA_TOPIC_STRING_POJO);
			Assertions.assertNotNull(singleRecord);
			Assertions.assertEquals(singleRecord.key(), key);
			Assertions.assertEquals(singleRecord.value(), messageLog);
		}
	}

	@Test
	void testProducerStringStringFilter() {

		// Given
		String key = "key";
		String value = "value";

		try (Consumer<String, String> consumer = this.configureConsumerStringString()) {

			// When
			producerStringString
					.send(new ProducerRecord<>(KafkaConstants.KAFKA_TOPIC_STRING_STRING_WITH_FILTER, key, value));

			// Then
			ConsumerRecord<String, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer,
					KafkaConstants.KAFKA_TOPIC_STRING_STRING_WITH_FILTER);
			Assertions.assertNotNull(singleRecord);
			Assertions.assertEquals(singleRecord.key(), key);
			Assertions.assertEquals(singleRecord.value(), value);
		}
	}

	private Consumer<String, String> configureConsumerStringString() {
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(KafkaConstants.KAFKA_GROUP_ID, "true",
				embeddedKafkaBroker);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		Consumer<String, String> consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps)
				.createConsumer();

		// Como uso este consumer para 2 test ya que son iguales uno con filtrado y otro
		// sin el pongo aqui los 2 topics
		// En este proyecto del producer al no tener acceso al consumidor como tal
		// (recuerda que tiene lógica de filtrado) este test se queda igual que el de un
		// String String normal
		consumer.subscribe(List.of(KafkaConstants.KAFKA_TOPIC_STRING_STRING,
				KafkaConstants.KAFKA_TOPIC_STRING_STRING_WITH_FILTER));
		return consumer;
	}

	private Consumer<String, MessageLog> configureConsumerStringPojo() {
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(KafkaConstants.KAFKA_GROUP_ID, "true",
				embeddedKafkaBroker);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.github.maquina1995.entity");

		Consumer<String, MessageLog> consumer = new DefaultKafkaConsumerFactory<String, MessageLog>(consumerProps)
				.createConsumer();

		consumer.subscribe(List.of(KafkaConstants.KAFKA_TOPIC_STRING_POJO));
		return consumer;
	}
}