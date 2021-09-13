package maquina1995.kafka.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import maquina1995.kafka.configuration.KafkaTopicConfig;
import maquina1995.kafka.constants.KafkaConstants;

/**
 * Podemos implementar varios listener para un determinado topic de kafka cada
 * uno con diferente groupId
 * <p>
 * Adicionalmente un consumer puede escuchar mensajes de mas de un topic
 * <p>
 * <b>Lecciones aprendidas:</b> 1 mensaje de un topic solo puede ser leido por
 * un consumer (Me he tirado varias horas con este problema intentando que
 * varios listener escucharan el mismo topic ;) )
 * 
 * @author MaQuiNa1995
 *
 */
@Slf4j
@Service
public class ConsumeMesssageService {

	/**
	 * Este listener se suscribe al topic indicado por:
	 * {@value KafkaConstants#KAFKA_TOPIC_NAME}
	 * 
	 * @param message mensaje recibido
	 */
	@KafkaListener(topics = KafkaConstants.KAFKA_TOPIC_NAME,
	        groupId = KafkaConstants.KAFKA_GROUP_ID)
	public void receiveMessageFromExampleTopic(String message) {

		log.info("[Listener-1] Mensaje recibido con grupo: " + KafkaConstants.KAFKA_GROUP_ID + " Topic: "
		        + KafkaConstants.KAFKA_TOPIC_NAME + " Mensaje: " + message);
	}

	/**
	 * Este listener se suscribe al topic indicado por:
	 * {@value KafkaConstants#KAFKA_TOPIC_NAME_FILTER} y ademas usa un filtro custom
	 * creado en el bean:
	 * {@link KafkaTopicConfig#filterKafkaListenerContainerFactory(org.springframework.kafka.core.ConsumerFactory)}
	 * 
	 * @param message mensaje filtrado recibido
	 */
	@KafkaListener(topics = KafkaConstants.KAFKA_TOPIC_NAME_FILTER,
	        containerFactory = "filterKafkaListenerContainerFactory")
	public void listenWithFilter(String message) {

		log.info("[Listener-2] Mensaje filtrado recibido con Grupo: " + KafkaConstants.KAFKA_GROUP_ID + " Topic: "
		        + KafkaConstants.KAFKA_TOPIC_NAME_FILTER + " Mensaje: " + message);
	}

}
