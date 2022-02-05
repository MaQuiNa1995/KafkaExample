package com.github.maquina1995.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.github.maquina1995.constants.KafkaConstants;

@Component
public class StringStringListener extends AbstractListener {

	/**
	 * Este listener se suscribe al topic indicado por:
	 * {@value KafkaConstants#KAFKA_TOPIC_STRING_STRING}
	 * 
	 * @param message mensaje recibido
	 */
	@KafkaListener(topics = KafkaConstants.KAFKA_TOPIC_STRING_STRING, groupId = KafkaConstants.KAFKA_GROUP_ID)
	public void receiveMessageFromExampleTopic(String message) {

		super.processMessage(1, message, KafkaConstants.KAFKA_TOPIC_STRING_STRING);
	}

}
