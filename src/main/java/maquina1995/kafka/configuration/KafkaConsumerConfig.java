package maquina1995.kafka.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import maquina1995.kafka.constants.KafkaConstants;
import maquina1995.kafka.service.ConsumeMesssageService;

/**
 * Para poder consumir mensajes de kafka necesitamos crear los siguientes beans
 * <li>{@link KafkaConsumerConfig#consumerFactory(String)}</li>
 * <li>{@link KafkaConsumerConfig#kafkaListenerContainerFactory()}</li>
 * <p>
 * y anotar nuestra clase con {@link EnableKafka} que sirve para la detección de
 * beans de spring anotados con
 * {@link org.springframework.kafka.annotation.KafkaListener}
 * <p>
 * un ejemplo: {@link ConsumeMesssageService}
 * 
 * @author MaQuiNa1995
 *
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

	/**
	 * Con este bean creamos la estrategia a seguir para crear los <b>Consumer
	 * instances</b> que son los objetos encargados de la lectura de registros de un
	 * cluster de kafka
	 * <p>
	 * 
	 * @param bootstrapAddress valor de la property <b>kafka.bootstrapAddress</b>
	 *                         inyectada desde el application.properties
	 * 
	 * @return {@link ConsumerFactory} configurado
	 */
	@Bean
	public ConsumerFactory<String, String> consumerFactory(
	        @Value(value = "${kafka.bootstrapAddress}") String bootstrapAddress) {

		Map<String, Object> properties = new HashMap<>();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstants.KAFKA_GROUP_ID);
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		return new DefaultKafkaConsumerFactory<>(properties);
	}

	/**
	 * Este método crea la implementación por defecto de los listener de kafka
	 * 
	 * @param consumerFactory bean de spring inyectado del contexto creado en
	 *                        {@link KafkaConsumerConfig#consumerFactory(String)}
	 *                        <p>
	 * @return {@link ConcurrentKafkaListenerContainerFactory} configurado
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
	        ConsumerFactory<String, String> consumerFactory) {

		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);

		return factory;
	}

	/**
	 * Mismo bean que {@link KafkaConsumerConfig#consumerFactory(String)} pero al
	 * que se le va a aplicar lógica de filtrado
	 * 
	 * @param bootstrapAddress valor de la property <b>kafka.bootstrapAddress</b>
	 *                         inyectada desde el application.properties
	 * 
	 * @return {@link ConsumerFactory} configurado
	 */
	@Bean
	public ConsumerFactory<String, String> consumerFactoryWithFilter(
	        @Value(value = "${kafka.bootstrapAddress}") String bootstrapAddress) {

		Map<String, Object> properties = new HashMap<>();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstants.KAFKA_GROUP_ID);
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		return new DefaultKafkaConsumerFactory<>(properties);
	}

	/**
	 * Este método crea la implementación por defecto de los listener de kafka
	 * 
	 * @param consumerFactory bean de spring inyectado del contexto creado en
	 *                        {@link KafkaConsumerConfig#consumerFactory(String)}
	 *                        <p>
	 * @return {@link ConcurrentKafkaListenerContainerFactory} configurado
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactoryWithFilter(
	        ConsumerFactory<String, String> consumerFactory) {

		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);

		return factory;
	}
}
