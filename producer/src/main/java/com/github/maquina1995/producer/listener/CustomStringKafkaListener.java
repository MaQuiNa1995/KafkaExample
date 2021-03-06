package com.github.maquina1995.producer.listener;

import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase representa un comportamiento custom para manejar los resultados
 * asincronos de los mensajes que se envian a kafka usando {@link String}
 * <p>
 * El constructor es por conveniencia mía para imprimir el mensaje por consola a
 * modo ejemplo no es obligatorio ni mucho menos
 * <p>
 * Los métodos se ejecutan según:
 * <li>Éxito: {@link CustomStringKafkaListener#onSuccess(SendResult)}</li>
 * <li>Problema: {@link CustomStringKafkaListener#onFailure(Throwable)}</li>
 * <p>
 * 
 * @author MaQuiNa1995
 */
@Slf4j
@RequiredArgsConstructor
public class CustomStringKafkaListener implements ListenableFutureCallback<SendResult<String, String>> {

	private final String message;

	@Override
	public void onSuccess(SendResult<String, String> result) {
		log.info("Mensaje enviado= [ " + this.message + " ] con offset=[ " + result.getRecordMetadata()
		        .offset() + " ]");
	}

	@Override
	public void onFailure(Throwable exception) {
		log.info("No se puede enviar el mensaje =[" + this.message + "] porque: " + exception.getMessage());
	}
}
