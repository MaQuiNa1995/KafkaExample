package maquina1995.kafka.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import maquina1995.kafka.configuration.KafkaTopicConfig;
import maquina1995.kafka.constants.KafkaConstants;
import maquina1995.kafka.messages.CustomMessage;

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

		this.logMessage(1, message, KafkaConstants.KAFKA_TOPIC_NAME);
	}

	/**
	 * Este listener se suscribe al topic indicado por:
	 * {@value KafkaConstants#KAFKA_TOPIC_NAME_WITH_FILTER} y ademas usa un filtro
	 * custom creado en el bean:
	 * {@link KafkaTopicConfig#filterKafkaListenerContainerFactory(org.springframework.kafka.core.ConsumerFactory)}
	 * 
	 * @param message mensaje filtrado recibido
	 */
	@KafkaListener(topics = KafkaConstants.KAFKA_TOPIC_NAME_WITH_FILTER,
	        containerFactory = "filterKafkaListenerContainerFactory")
	public void listenWithFilter(String message) {

		this.logMessage(2, message, KafkaConstants.KAFKA_TOPIC_NAME_WITH_FILTER);
	}

	/**
	 * Este listener se suscribe al topic indicado por:
	 * {@value KafkaConstants#KAFKA_TOPIC_NAME_WITH_POJO} y ademas usa un filtro
	 * custom creado en el bean:
	 * {@link KafkaTopicConfig#filterKafkaListenerContainerFactory(org.springframework.kafka.core.ConsumerFactory)}
	 * 
	 * @param message mensaje filtrado recibido
	 */
	@KafkaListener(topics = KafkaConstants.KAFKA_TOPIC_NAME_WITH_POJO,
	        containerFactory = "pojoKafkaListenerContainerFactory")
	public void greetingListener(CustomMessage customMessage) {

		this.logMessage(3, customMessage.toString(), KafkaConstants.KAFKA_TOPIC_NAME_WITH_POJO);
	}

	private void logMessage(int listenerNumber, String message, String topic) {

		log.info("[Listener-" + listenerNumber + "] Mensaje recibido con grupo: " + KafkaConstants.KAFKA_GROUP_ID
		        + " Topic: " + topic + " Mensaje: " + message);
	}

}
