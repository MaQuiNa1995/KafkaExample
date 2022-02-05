package com.github.maquina1995.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.github.maquina1995.configuration.KafkaTopicConfig;
import com.github.maquina1995.constants.KafkaConstants;
import com.github.maquina1995.entity.MessageLog;

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
@Component
public class StringPojoListener extends AbstractListener {

	/**
	 * Este listener se suscribe al topic indicado por:
	 * {@value KafkaConstants#KAFKA_TOPIC_STRING_POJO} y ademas usa un filtro custom
	 * creado en el bean:
	 * {@link KafkaTopicConfig#filterKafkaListenerContainerFactory(org.springframework.kafka.core.ConsumerFactory)}
	 * 
	 * @param message mensaje filtrado recibido
	 */
	@KafkaListener(topics = KafkaConstants.KAFKA_TOPIC_STRING_POJO, containerFactory = "pojoKafkaListenerContainerFactory")
	public void greetingListener(MessageLog customMessage) {

		super.processMessage(3, customMessage.toString(), KafkaConstants.KAFKA_TOPIC_STRING_POJO);
	}

}
