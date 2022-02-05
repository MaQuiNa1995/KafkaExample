package com.github.maquina1995.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import com.github.maquina1995.constants.KafkaConstants;

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
 * <b>Lecciones Aprendidas:</b> los nombres de los topic no deben tener espacios
 * 
 * @author MaQuiNa1995
 *
 */
@SpringBootConfiguration
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
		return TopicBuilder.name(KafkaConstants.KAFKA_TOPIC_STRING_STRING).partitions(1).replicas(1).build();
	}

	/**
	 * Con este método otro topic desde nuestro java, con fines de ejemplo para
	 * mostrar el filtro de los mismos
	 * 
	 * @return {@link NewTopic} topic creado programaticalmente
	 */
	@Bean
	public NewTopic topic2() {
		return TopicBuilder.name(KafkaConstants.KAFKA_TOPIC_STRING_STRING_WITH_FILTER).partitions(1).replicas(1)
				.build();
	}

}
