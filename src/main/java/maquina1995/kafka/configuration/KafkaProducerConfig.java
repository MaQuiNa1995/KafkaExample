package maquina1995.kafka.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import maquina1995.kafka.messages.CustomMessage;

/**
 * Clase para enseñar la configuración relacionada a los Producer de kafka (Que
 * son los que producen y envían mensajes )
 * 
 * @author MaQuiNa1995
 *
 */
@Configuration
public class KafkaProducerConfig {

	/**
	 * Con este bean creamos la estrategia a seguir para crear los <b>Producer
	 * instances</b> que son los objetos encargados de la publicación de registros a
	 * un cluster de kafka
	 * <p>
	 * Como dato adicional los <b>Producer instances</b> son Thread-safe por lo
	 * tanto es recomendable que solo haya una instancia de el en el programa (Como
	 * tenemos aqui que el bean es singleton)
	 * 
	 * @param bootstrapAddress valor de la property <b>kafka.bootstrapAddress</b>
	 *                         inyectada desde el application.properties
	 * 
	 * @return {@link ProducerFactory} < String, String > configurado
	 */
	@Bean
	public ProducerFactory<String, String> producerFactory(
	        @Value(value = "${kafka.bootstrapAddress}") String bootstrapAddress) {

		Map<String, Object> configProps = new HashMap<>(3);
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		return new DefaultKafkaProducerFactory<>(configProps);
	}

	/**
	 * Se usa como envoltorio (Wrapper) de los <b>Producer instances</b> que ya
	 * hemos creado previamente (mirar el parámetro producerFactory) y que provee de
	 * métodos útiles y comunes para enviar mensajes a determinados topics de kafka
	 * 
	 * @param producerFactory bean obtenido del contexto de spring que hace
	 *                        referencia al return de
	 *                        {@link KafkaProducerConfig#producerFactory(String)}
	 * @return {@link KafkaTemplate} < {@link String} , {@link String} >
	 */
	@Bean
	public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}

	/**
	 * Creamos este bean para poder mandar mensajes con un determinado pojo en este
	 * caso {@link CustomMessage}
	 * <p>
	 * Funciona igual que {@link KafkaProducerConfig#producerFactory} solo que este
	 * no maneja mensajes con {@link String} planos sino con {@link CustomMessage}
	 * 
	 * @param bootstrapAddress valor de la property <b>kafka.bootstrapAddress</b>
	 *                         inyectada desde el application.properties
	 * @return {@link ProducerFactory} < {@link String}, {@link CustomMessage} >
	 *         configurado
	 */
	@Bean
	public ProducerFactory<String, CustomMessage> customPojoProducerFactory(
	        @Value(value = "${kafka.bootstrapAddress}") String bootstrapAddress) {

		Map<String, Object> configProps = new HashMap<>(3);
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	/**
	 * Funciona igual que {@link KafkaProducerConfig#kafkaTemplate(ProducerFactory)}
	 * peo en vez de con {@link String} con {@link CustomMessage}
	 * 
	 * @param producerFactory bean obtenido del contexto de spring que hace
	 *                        referencia al return de
	 *                        {@link KafkaProducerConfig#customPojoProducerFactory(String)}
	 * @return {@link KafkaTemplate} < {@link String} , {@link CustomMessage} >
	 */
	@Bean
	public KafkaTemplate<String, CustomMessage> kafkaTemplateWithCustomPojo(
	        ProducerFactory<String, CustomMessage> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}
}
