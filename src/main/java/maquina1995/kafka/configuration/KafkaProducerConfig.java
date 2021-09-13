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
	 * @param bootstrapAddress
	 * 
	 * @return
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
	 * @param producerFactory bean obtenido del contexto de spring que hare
	 *                        referencia al return de
	 *                        {@link KafkaProducerConfig#producerFactory(String)}
	 * @return
	 */
	@Bean
	public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}
}
