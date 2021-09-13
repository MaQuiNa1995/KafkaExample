package maquina1995.kafka.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import maquina1995.kafka.constants.KafkaConstants;

/**
 * Podemos implementar varios listener para un determinado topic de kafka cada
 * uno con diferente groupId
 * <p>
 * Adicionalmente un consumer puede escuchar mensajes de mas de un topic
 * 
 * @author MaQuiNa1995
 *
 */
@Service
public class ConsumeMesssageService {

	@KafkaListener(topics = KafkaConstants.KAFKA_TOPIC_EXAMPLE,
	        groupId = KafkaConstants.KAFKA_GROUP_ID)
	public void receiveMessageFromExampleTopic(String message) {

		System.out.println("Mensaje recibido con grupo: " + KafkaConstants.KAFKA_GROUP_ID + " Topic: "
		        + KafkaConstants.KAFKA_TOPIC_EXAMPLE + " y mensaje: " + message);
	}

}
