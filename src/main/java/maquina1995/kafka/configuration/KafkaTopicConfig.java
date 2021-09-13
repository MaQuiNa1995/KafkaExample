package maquina1995.kafka.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

import maquina1995.kafka.constants.KafkaConstants;

/**
 * Para crear <b>topics de kafka</b> podemos recurrir a 2 estrategias:
 * 
 * <li>Crearlos programaticamente en nuestro java</li> Un ejemplo es esta misma
 * clase: {@link KafkaTopicConfig} <br />
 * <p>
 * 
 * <li>Directamente en la instancia de kafka</li> <b>Linux:</b>
 * bin/kafka-topics.sh <br />
 * <b>Windows:</b> bin/windows/kafka-topics.bat <br />
 * seguido de: <br />
 * --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1
 * --topic mytopic
 * <p>
 * 
 * @author MaQuiNa1995
 *
 */
@Configuration
public class KafkaTopicConfig {

	/**
	 * Es el objeto encargado de la creación de topics
	 * <p>
	 * Si usas Springboot (como nosotros) no hace falta que lo definas pero para
	 * fines didácticos lo dejo :)
	 * 
	 * @param bootstrapAddress valor de la property <b>kafka.bootstrapAddress</b>
	 *                         inyectada desde el application.properties
	 * 
	 * @return {@link KafkaAdmin} configurado
	 */
	@Bean
	public KafkaAdmin kafkaAdmin(@Value(value = "${kafka.bootstrapAddress}") String bootstrapAddress) {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);

		return new KafkaAdmin(configs);
	}

	/**
	 * Con este método crearíamos un topic desde nuestro java
	 * 
	 * @return {@link NewTopic} topic creado programaticalmente
	 */
	@Bean
	public NewTopic topic1() {
		return TopicBuilder.name(KafkaConstants.KAFKA_TOPIC_NAME)
		        .partitions(1)
		        .replicas(1)
		        .build();
	}

	/**
	 * Con este método otro topic desde nuestro java, con fines de ejemplo para
	 * mostrar el filtro de los mismos
	 * 
	 * @return {@link NewTopic} topic creado programaticalmente
	 */
	@Bean
	public NewTopic topic2() {
		return TopicBuilder.name(KafkaConstants.KAFKA_TOPIC_NAME_FILTER)
		        .partitions(1)
		        .replicas(1)
		        .build();
	}

	/**
	 * Configura el {@link ConsumerFactory} inyectado en spring para definir una/s
	 * determinadas reglas de filtrado
	 * <p>
	 * En este caso la regla está definida en:
	 * {@link KafkaTopicConfig#createCustomRecordFilterStrategy()}
	 * <p>
	 * Se ha configurado una regla de cargado del bean para que cuando esté la
	 * property <b>custom.filter.enabled<b> a <b>true<b> se inyecte al contexto
	 * 
	 * @param consumerFactory {@link ConsumerFactory} inyectado en el contexto desde
	 *                        {@link KafkaConsumerConfig#consumerFactoryWithFilter(String)}
	 *                        <p>
	 * @return {@link ConcurrentKafkaListenerContainerFactory} configurado
	 */
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> filterKafkaListenerContainerFactory(
	        ConsumerFactory<String, String> consumerFactoryWithFilter) {

		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactoryWithFilter);
		factory.setRecordFilterStrategy(this.createCustomRecordFilterStrategy());
		return factory;
	}

	/**
	 * Este método crea una estrategia de filtrado para que solo acepte (en este
	 * caso) los mensajes de kafka que sean iguales a un determinado valor en el
	 * value osea en el mensaje
	 * <p>
	 * Si, la descripcióne s correcta para esta condición: <b>!"Mensaje asincrono
	 * con logica de filtrado".equals(kafkaRecord.value())</b>
	 * <p>
	 * Por asi decirlo la condición que tu definas en el lambda se niega
	 * 
	 * @return {@link RecordFilterStrategy}< String, String > configurado
	 */
	private RecordFilterStrategy<String, String> createCustomRecordFilterStrategy() {
		return kafkaRecord -> !"Mensaje asincrono con logica de filtrado".equals(kafkaRecord.value());
	}
}
