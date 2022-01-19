package com.github.maquina1995.kafka;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import com.github.maquina1995.kafka.constants.KafkaConstants;
import com.github.maquina1995.kafka.entity.MessageLog;
import com.github.maquina1995.kafka.repository.MessageLogRepository;

@EnableKafka
@SpringBootTest
@EmbeddedKafka(partitions = 1,
        controlledShutdown = true,
        brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" }
// ,topics = { KafkaConstants.KAFKA_TOPIC_NAME_WITH_POJO }
)
class ConsumerMesssageServiceTest {

	@Autowired
	private MessageLogRepository messageLogRepository;

	@Autowired
	private KafkaTemplate<String, MessageLog> template;

	@Test
	void consumerListenerTest() throws Exception {

		// Given
		MessageLog customMessage = new MessageLog("test");

		// When
		template.send(KafkaConstants.KAFKA_TOPIC_NAME_WITH_POJO, customMessage);

		// Then
		messageLogRepository.findAll();
		Assertions.assertTrue(messageLogRepository.findByMessage("test")
		        .isPresent());

	}
}
//@EmbeddedKafka(partitions = 1,
//        controlledShutdown = false,
//        brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" },
//        topics = { "testTopic" })
//@SpringBootTest
//class SimpleKafkaTest {
//
//	private static final String TEST_TOPIC = "testTopic";
//
//	@Autowired
//	private EmbeddedKafkaBroker embeddedKafkaBroker;
//
//	@Test
//	void testReceivingKafkaEvents() {
//
//		try (Consumer<Integer, String> consumer = configureConsumer();
//		        Producer<Integer, String> producer = configureProducer()) {
//
//			producer.send(new ProducerRecord<>(TEST_TOPIC, 123, "my-test-value"));
//
//			ConsumerRecord<Integer, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer, TEST_TOPIC);
//			Assertions.assertNotNull(singleRecord);
//			Assertions.assertEquals(123, singleRecord.key());
//			Assertions.assertEquals("my-test-value", singleRecord.value());
//		}
//	}
//
//	private Consumer<Integer, String> configureConsumer() {
//		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
//		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//		Consumer<Integer, String> consumer = new DefaultKafkaConsumerFactory<Integer, String>(consumerProps)
//		        .createConsumer();
//		consumer.subscribe(Collections.singleton(TEST_TOPIC));
//		return consumer;
//	}
//
//	private Producer<Integer, String> configureProducer() {
//		Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
//		return new DefaultKafkaProducerFactory<Integer, String>(producerProps).createProducer();
//	}
//}