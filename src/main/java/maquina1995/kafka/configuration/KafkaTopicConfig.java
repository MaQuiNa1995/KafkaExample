package maquina1995.kafka.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import maquina1995.kafka.constants.KafkaConstants;

/**
 * para crear <b>topics de kafka</b> podemos recurrir a 2 estrategias:
 * 
 * <li>Crearlos programaticamente en nuestro java</li> Un ejemplo es esta misma
 * clase: {@link KafkaTopicConfig} <br />
 * <p>
 * 
 * <li>Directamente en la instancia de kafka</li> <b>Linux:</b>
 * bin/kafka-topics.sh <br />
 * <b>Windows:</b> bin/windows/kafka-topics.bat --create --zookeeper
 * localhost:2181 --replication-factor 1 --partitions 1 --topic mytopic
 * <p>
 * 
 * @author MaQuiNa1995
 *
 */
@Configuration
public class KafkaTopicConfig {

	@Bean
	public KafkaAdmin kafkaAdmin(@Value(value = "${kafka.bootstrapAddress}") String bootstrapAddress) {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);

		return new KafkaAdmin(configs);
	}

	@Bean
	public NewTopic topic1() {
		return new NewTopic(KafkaConstants.KAFKA_TOPIC_EXAMPLE, 1, (short) 1);
	}
}
