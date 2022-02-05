package com.github.maquina1995;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import com.github.maquina1995.configuration.KafkaConsumerConfig;
import com.github.maquina1995.constants.KafkaConstants;
import com.github.maquina1995.entity.MessageLog;
import com.github.maquina1995.listener.AbstractListener;

@EmbeddedKafka(partitions = 1, ports = 9092)
@EnableKafka
@ComponentScan(basePackages = "com.github.maquina1995")
@SpringBootTest(classes = { Main.class })
class ConsumerTest {

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	@Resource(name = "stringStringListener")
	private AbstractListener listenerStringString;
	@Resource(name = "stringStringWithFilterListener")
	private AbstractListener listenerStringStringFilter;
	@Resource(name = "stringPojoListener")
	private AbstractListener listenerStringPojo;

	@Test
	void testConsumerStringString() throws InterruptedException {

		// Given
		String key = "key1";
		String value = "value1";

		try (Producer<String, String> producer = this.configureProducerStringString()) {

			// When
			producer.send(new ProducerRecord<>(KafkaConstants.KAFKA_TOPIC_STRING_STRING, key, value));

			listenerStringString.getLatch().await(3, TimeUnit.SECONDS);

			Assertions.assertEquals(9L, listenerStringString.getLatch().getCount());
			Assertions.assertEquals(value, listenerStringString.getPayload());
		}
	}

	@Test
	void testConsumerStringStringWithValidFilter() throws InterruptedException {

		// Given
		String key = "key2";

		CountDownLatch counter = listenerStringStringFilter.getLatch();
		counter.countDown();

		try (Producer<String, String> producer = this.configureProducerStringStringWithFilter()) {

			// When
			producer.send(new ProducerRecord<>(KafkaConstants.KAFKA_TOPIC_STRING_STRING_WITH_FILTER, key,
					KafkaConsumerConfig.STRING_TO_FILTER));

			listenerStringStringFilter.getLatch().await(3, TimeUnit.SECONDS);

			// Then
			// Comprobamos que hubo interaccion exactamente de 1
			Assertions.assertEquals(counter.getCount(), listenerStringStringFilter.getLatch().getCount());
			Assertions.assertEquals(KafkaConsumerConfig.STRING_TO_FILTER, listenerStringStringFilter.getPayload());
		}
	}

	@Test
	void testConsumerStringStringWithInValidFilter() throws InterruptedException {

		// Given
		String key = "key2";
		String value = "invalidValueToFilter";

		CountDownLatch counter = listenerStringStringFilter.getLatch();

		try (Producer<String, String> producer = this.configureProducerStringStringWithFilter()) {

			// When
			producer.send(new ProducerRecord<>(KafkaConstants.KAFKA_TOPIC_STRING_STRING_WITH_FILTER, key, value));

			listenerStringStringFilter.getLatch().await(3, TimeUnit.SECONDS);

			// Then
			// Comprobamos que no hubo interaccion
			Assertions.assertEquals(counter.getCount(), listenerStringStringFilter.getLatch().getCount());
		}
	}

	@Test
	void testConsumerStringPojo() throws InterruptedException {

		// Given
		String key = "key3";
		MessageLog message = MessageLog.builder().message("mensaje").build();

		try (Producer<String, MessageLog> producer = this.configureProducerStringPojo()) {

			// When
			producer.send(new ProducerRecord<>(KafkaConstants.KAFKA_TOPIC_STRING_POJO, key, message));

			listenerStringString.getLatch().await(3, TimeUnit.SECONDS);

			Assertions.assertEquals(9L, listenerStringPojo.getLatch().getCount());
			Assertions.assertEquals(message.toString(), listenerStringPojo.getPayload());
		}
	}

	private Producer<String, String> configureProducerStringString() {
		Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));

		// Debido a que KafkaTestUtils.producerProps hace por defecto que key.serializer
		// sea un IntegerSerializer hay que sobreescribir esta propiedad ya que nosotros
		// usamos una String para la key
		producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		return new DefaultKafkaProducerFactory<String, String>(producerProps).createProducer();
	}

	private Producer<String, String> configureProducerStringStringWithFilter() {
		Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));

		// Debido a que KafkaTestUtils.producerProps hace por defecto que key.serializer
		// sea un IntegerSerializer hay que sobreescribir esta propiedad ya que nosotros
		// usamos una String para la key
		producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		return new DefaultKafkaProducerFactory<String, String>(producerProps).createProducer();
	}

	private Producer<String, MessageLog> configureProducerStringPojo() {
		Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));

		// Debido a que KafkaTestUtils.producerProps hace por defecto que key.serializer
		// sea un IntegerSerializer hay que sobreescribir esta propiedad ya que nosotros
		// usamos una String para la key
		producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		// aparte tambien debemos configurar el serializador del valor ya que este por
		// defecto es String
		producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		return new DefaultKafkaProducerFactory<String, MessageLog>(producerProps).createProducer();
	}

}