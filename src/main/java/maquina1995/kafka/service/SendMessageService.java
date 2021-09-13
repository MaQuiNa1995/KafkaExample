package maquina1995.kafka.service;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import maquina1995.kafka.constants.KafkaConstants;
import maquina1995.kafka.listener.CustomKafkaListener;

/**
 * Para enviar un mensaje a kafka tenemos 2 alternativas <br />
 * Asíncrona: {@link SendMessageService#sendAsynchronousMessage(String)} <br />
 * Síncrona: {@link SendMessageService#sendSynchronousMessage(String)}
 * 
 * @author MaQuiNa1995
 */
@Service
public class SendMessageService {

	/**
	 * Usamos este objeto inyectado del contexto para por ejemplo el envío de
	 * mensajes a kafka
	 */
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	/**
	 * En este método al usar un listener custom podemos definir el comportamiento
	 * de la recepción exitosa o no del envío: mas info en: {@link CustomKafkaListener}
	 * 
	 * @param asynchronousMessage mensaje a enviar a kafka
	 */
	public void sendAsynchronousMessage(String asynchronousMessage) {

		ListenableFuture<SendResult<String, String>> asynchronousResult = kafkaTemplate
		        .send(KafkaConstants.KAFKA_TOPIC_EXAMPLE, asynchronousMessage);

		asynchronousResult.addCallback(new CustomKafkaListener(asynchronousMessage));
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
	public void sendSynchronousMessage(String synchronousMessage) throws InterruptedException, ExecutionException {

		ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate
		        .send(KafkaConstants.KAFKA_TOPIC_EXAMPLE, synchronousMessage);

		SendResult<String, String> synchronousResult = listenableFuture.get();

		System.out.println("Resultado: " + synchronousResult);
	}

}
