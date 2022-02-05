package com.github.maquina1995;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import com.github.maquina1995.producer.Main;

/**
 * <b>Lecciones Aprendidas:</b> Aquí debemos usar el puerto que tenemos
 * configurado en nuestro properties para que asi sea igual el del embebed kafka
 * y el que tendríamos configurado por la property
 * <p>
 * En este caso no necesitamos el puerto no lo estamos usando ya qe estamos
 * creando en este mismo test el producer y el consumer nos da igual que sea uno
 * aleatorio (que por defecto es asi)
 * 
 * @author MaQuiNa1995
 *
 */
@EmbeddedKafka(partitions = 1, topics = SimpleKafkaTest.TOPIC)
@SpringBootTest(classes = { Main.class })
class SimpleKafkaTest {

	/**
	 * Lo ponemos protected para poder usarlo en la anotación @EmbeddedKafka en la
	 * línea 34
	 */
	protected static final String TOPIC = "prueba";
	private static final String KAFKA_GROUP = "testGroup";

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	@Test
	void testReceivingKafkaEvents() {

		embeddedKafkaBroker.getBrokerAddresses();

		try (Consumer<Integer, String> consumer = this.configureConsumer();
				Producer<Integer, String> producer = this.configureProducer()) {

			producer.send(new ProducerRecord<>(TOPIC, 123, "my-test-value"));

			ConsumerRecord<Integer, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer, TOPIC);
			Assertions.assertNotNull(singleRecord);
			Assertions.assertEquals(singleRecord.key(), 123);
			Assertions.assertEquals(singleRecord.value(), "my-test-value");
		}
	}

	private Consumer<Integer, String> configureConsumer() {
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(KAFKA_GROUP, "true", embeddedKafkaBroker);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		Consumer<Integer, String> consumer = new DefaultKafkaConsumerFactory<Integer, String>(consumerProps)
				.createConsumer();

		consumer.subscribe(List.of(TOPIC));
		return consumer;
	}

	private Producer<Integer, String> configureProducer() {
		Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
		return new DefaultKafkaProducerFactory<Integer, String>(producerProps).createProducer();
	}
}