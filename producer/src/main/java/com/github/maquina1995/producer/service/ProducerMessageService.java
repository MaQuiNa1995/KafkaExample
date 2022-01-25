package com.github.maquina1995.producer.service;

import java.util.concurrent.ExecutionException;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import com.github.maquina1995.constants.KafkaConstants;
import com.github.maquina1995.entity.MessageLog;
import com.github.maquina1995.producer.listener.CustomPojoKafkaListener;
import com.github.maquina1995.producer.listener.CustomStringKafkaListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Para enviar un mensaje a kafka tenemos 2 alternativas <br />
 * Asíncrona: {@link ProducerMessageService#sendAsynchronousMessage(String)}
 * <br />
 * Síncrona: {@link ProducerMessageService#sendSynchronousStringMessage(String)}
 * 
 * @author MaQuiNa1995
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerMessageService {

	/**
	 * Usamos este objeto inyectado del contexto para por ejemplo el envío de
	 * mensajes a kafka con {@link String}, {@link String}
	 */
	private final KafkaTemplate<String, String> kafkaTemplate;
	/**
	 * Usamos este objeto inyectado del contexto para por ejemplo el envío de
	 * mensajes a kafka con {@link String}, {@link String}
	 */
	private final KafkaTemplate<String, MessageLog> kafkaTemplateWithPojo;

	/**
	 * En este método al usar un listener custom podemos definir el comportamiento
	 * de la recepción exitosa o no del envío
	 * 
	 * @see {@link CustomStringKafkaListener}
	 * 
	 * @param asynchronousMessage {@link String} a enviar a kafka
	 * @param topic               topic al que enviar el mensaje
	 */
	public void sendAsynchronousStringMessage(String asynchronousMessage, String topic) {

		// mensaje con topic y mensaje formato String para consumer sin filtrar
		ListenableFuture<SendResult<String, String>> asynchronousResult = kafkaTemplate.send(topic,
				asynchronousMessage);

		asynchronousResult.addCallback(new CustomStringKafkaListener(asynchronousMessage));

	}

	/**
	 * En este método al usar un listener custom podemos definir el comportamiento
	 * de la recepción exitosa o no del envío
	 * 
	 * @see {@link CustomPojoKafkaListener}
	 * 
	 * @param customMessage {@link Message} a enviar a kafka
	 * @param topic         topic al que enviar el mensaje
	 */
	public void sendAsynchronousPojoMessage(MessageLog customMessage, String topic) {

		// mensaje con topic y mensaje formato String para consumer con filtro
		ListenableFuture<SendResult<String, MessageLog>> asynchronousResult = kafkaTemplateWithPojo.send(topic,
				customMessage);

		asynchronousResult.addCallback(new CustomPojoKafkaListener(customMessage));

	}

	/**
	 * En este método al usar {@link ListenableFuture#get()} bloqueamos la ejecución
	 * ya que el hilo quedaría pendiente de la recepción de la respuesta
	 * <p>
	 * esto claramente puede hacer mas lenta la ejecución, la mejor opción es usar
	 * la opción asíncrona
	 * 
	 * @param synchronousMessage mensaje a enviar a kafka
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public void sendSynchronousStringMessage(String synchronousMessage)
			throws InterruptedException, ExecutionException {

		ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate
				.send(KafkaConstants.KAFKA_TOPIC_NAME, synchronousMessage);

		SendResult<String, String> synchronousResult = listenableFuture.get();

		log.info("Resultado: " + synchronousResult);
	}
}
