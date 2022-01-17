package com.github.maquina1995.kafka;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import com.github.maquina1995.kafka.constants.KafkaConstants;
import com.github.maquina1995.kafka.messages.CustomMessage;
import com.github.maquina1995.kafka.service.ConsumerMesssageService;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1,
        controlledShutdown = false,
        brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" },
        topics = { KafkaConstants.KAFKA_TOPIC_NAME })
class ConsumerMesssageServiceTest {

	@SpyBean
	private ConsumerMesssageService consumerMesssageService;

	@Autowired
	private KafkaTemplate<String, CustomMessage> template;

	@Captor
	private ArgumentCaptor<CustomMessage> argumentCaptor;

	@Test
	void consumerListenerTest() throws Exception {

		// Given
		CustomMessage customMessage = CustomMessage.builder()
		        .message("test")
		        .build();

		// When
		template.send(KafkaConstants.KAFKA_TOPIC_NAME_WITH_POJO, customMessage);

		// Then
		Mockito.verify(consumerMesssageService, Mockito.times(1))
		        .greetingListener(customMessage);
		Mockito.verify(consumerMesssageService)
		        .greetingListener(argumentCaptor.capture());
		Assertions.assertEquals(customMessage, argumentCaptor.getValue());

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