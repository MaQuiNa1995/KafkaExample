package com.github.maquina1995.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.github.maquina1995.configuration.KafkaTopicConfig;
import com.github.maquina1995.constants.KafkaConstants;

@Component
public class StringStringWithFilterListener extends AbstractListener {

	/**
	 * Este listener se suscribe al topic indicado por:
	 * {@value KafkaConstants#KAFKA_TOPIC_STRING_STRING_WITH_FILTER} y ademas usa un
	 * filtro custom creado en el bean:
	 * {@link KafkaTopicConfig#filterKafkaListenerContainerFactory(org.springframework.kafka.core.ConsumerFactory)}
	 * 
	 * @param message mensaje filtrado recibido
	 */
	@KafkaListener(topics = KafkaConstants.KAFKA_TOPIC_STRING_STRING_FILTER, containerFactory = "filterKafkaListenerContainerFactory")
	public void listenWithFilter(String message) {

		super.processMessage(2, message, KafkaConstants.KAFKA_TOPIC_STRING_STRING_FILTER);
	}

}
