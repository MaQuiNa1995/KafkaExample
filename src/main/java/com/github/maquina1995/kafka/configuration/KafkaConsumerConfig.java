package com.github.maquina1995.kafka.configuration;

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
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.github.maquina1995.kafka.constants.KafkaConstants;
import com.github.maquina1995.kafka.entity.MessageLog;
import com.github.maquina1995.kafka.service.ConsumerMesssageService;

/**
 * Para poder consumir mensajes de kafka necesitamos crear los siguientes beans
 * <li>{@link KafkaConsumerConfig#consumerFactory(String)}</li>
 * <li>{@link KafkaConsumerConfig#kafkaListenerContainerFactory()}</li>
 * <p>
 * Y anotar nuestra clase con {@link EnableKafka} que sirve para la detección de
 * beans de spring anotados con
 * {@link org.springframework.kafka.annotation.KafkaListener}
 * <p>
 * Un ejemplo: {@link ConsumerMesssageService}
 * <p>
 * A continuación un indice visual de lo que tiene esta clase:
 * <p>
 * <table border="2">
 * <tr align="center">
 * <td>Tipo De Mensaje</td>
 * <td>Con Filtro</td>
 * <td>Bean {@link ConsumerFactory}</td>
 * <td>Bean {@link ConcurrentKafkaListenerContainerFactory}</td>
 * </tr>
 * <tr align="center">
 * <td>{@link String}</td>
 * <td>No</td>
 * <td>{@link KafkaConsumerConfig#consumerFactory(String)}</td>
 * <td>{@link KafkaConsumerConfig#kafkaListenerContainerFactory(ConsumerFactory)}</td>
 * </tr>
 * <tr align="center">
 * <td>{@link String}</td>
 * <td>Si</td>
 * <td>{@link KafkaConsumerConfig#consumerFactoryWithFilter(String)}</td>
 * <td>{@link KafkaConsumerConfig#kafkaListenerContainerFactoryWithFilter(ConsumerFactory)}</td>
 * </tr>
 * <tr align="center">
 * <td>{@link Message}</td>
 * <td>No</td>
 * <td>{@link KafkaConsumerConfig#consumerFactoryWithPojo(String)}</td>
 * <td>{@link KafkaConsumerConfig#kafkaListenerContainerFactoryWithPojo(ConsumerFactory)}</td>
 * </tr>
 * </table>
 * 
 * @author MaQuiNa1995
 *
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

	@Value(value = "${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	// ---------------------- Consumer para String plano ---------------------

	/**
	 * Con este bean creamos la estrategia a seguir para crear los <b>Consumer
	 * instances</b> que son los objetos encargados de la lectura de registros de un
	 * cluster de kafka
	 * <p>
	 * 
	 * @param bootstrapAddress valor de la property <b>kafka.bootstrapAddress</b>
	 *                         inyectada desde el application.properties
	 * 
	 * @return {@link ConsumerFactory} < {@link String} , {@link String} >
	 *         configurado
	 */
	@Bean
	public ConsumerFactory<String, String> consumerFactory() {

		Map<String, Object> properties = this.createKafkaProperties();

		return new DefaultKafkaConsumerFactory<>(properties);
	}

	/**
	 * Este método crea la implementación por defecto de los listener de kafka
	 * 
	 * @param consumerFactory bean de spring inyectado del contexto creado en
	 *                        {@link KafkaConsumerConfig#consumerFactory(String)}
	 *                        <p>
	 * @return {@link ConcurrentKafkaListenerContainerFactory} < {@link String} ,
	 *         {@link String} > configurado
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
	        ConsumerFactory<String, String> consumerFactory) {

		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);

		return factory;
	}

	// ------------ Consumer para String plano con filtro custom ------------

	/**
	 * Mismo bean que {@link KafkaConsumerConfig#consumerFactory(String)} pero al
	 * que se le va a aplicar lógica de filtrado
	 * 
	 * @param bootstrapAddress valor de la property <b>kafka.bootstrapAddress</b>
	 *                         inyectada desde el application.properties
	 * 
	 * @return {@link ConsumerFactory} < {@link String} , {@link String} >
	 *         configurado
	 */
	@Bean
	public ConsumerFactory<String, String> consumerFactoryWithFilter() {

		Map<String, Object> properties = this.createKafkaProperties();

		return new DefaultKafkaConsumerFactory<>(properties);
	}

	/**
	 * Este método crea la implementación por defecto de los listener de kafka
	 * 
	 * @param consumerFactory bean de spring inyectado del contexto creado en
	 *                        {@link KafkaConsumerConfig#consumerFactoryWithFilter(String)}
	 *                        <p>
	 * @return {@link ConcurrentKafkaListenerContainerFactory} < {@link String} ,
	 *         {@link String} > configurado
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactoryWithFilter(
	        ConsumerFactory<String, String> consumerFactory) {

		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);

		return factory;
	}

	// -------------------- Consumer para Pojo ------------------------

	/**
	 * Mismo bean que {@link KafkaConsumerConfig#consumerFactory(String)} pero que
	 * va a procesar un pojo {@link Message} en vez de una {@link String}
	 * 
	 * @param bootstrapAddress valor de la property <b>kafka.bootstrapAddress</b>
	 *                         inyectada desde el application.properties
	 * 
	 * @return {@link ConsumerFactory} < {@link String} , {@link Message} >
	 *         configurado
	 */
	@Bean
	public ConsumerFactory<String, MessageLog> consumerFactoryWithPojo() {

		Map<String, Object> properties = createKafkaProperties();

		return new DefaultKafkaConsumerFactory<>(properties, new StringDeserializer(),
		        new JsonDeserializer<>(MessageLog.class));
	}

	/**
	 * Este método crea la implementación por defecto de los listener de kafka
	 * 
	 * @param consumerFactory bean de spring inyectado del contexto creado en
	 *                        {@link KafkaConsumerConfig#consumerFactoryWithPojo(String)}
	 *                        <p>
	 * @return {@link ConcurrentKafkaListenerContainerFactory} < {@link String} ,
	 *         {@link Message} > configurado
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, MessageLog> kafkaListenerContainerFactoryWithPojo(
	        ConsumerFactory<String, MessageLog> consumerFactory) {

		ConcurrentKafkaListenerContainerFactory<String, MessageLog> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		return factory;
	}

	/**
	 * Creación de las properties de kafka
	 * 
	 * @param bootstrapAddress
	 * @return
	 */
	private Map<String, Object> createKafkaProperties() {
		Map<String, Object> properties = new HashMap<>();
		// Aqui se configura el puerto de kafka
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		// Se configura el id del grupo al que se va a suscribir el consumer
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstants.KAFKA_GROUP_ID);
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return properties;
	}
}
