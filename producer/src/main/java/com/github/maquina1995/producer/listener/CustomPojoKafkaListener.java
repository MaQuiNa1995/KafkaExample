package com.github.maquina1995.producer.listener;

import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.github.maquina1995.entity.MessageLog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase representa un comportamiento custom para manejar los resultados
 * asincronos de los mensajes que se envian a kafka usando un pojo
 * {@link Message}
 * <p>
 * El constructor es por conveniencia mía para imprimir el mensaje por consola a
 * modo ejemplo no es obligatorio ni mucho menos
 * <p>
 * Los métodos se ejecutan según:
 * <li>Éxito: {@link CustomPojoKafkaListener#onSuccess(SendResult)}</li>
 * <li>Problema: {@link CustomPojoKafkaListener#onFailure(Throwable)}</li>
 * 
 * @author MaQuiNa1995
 */
@Slf4j
@RequiredArgsConstructor
public class CustomPojoKafkaListener implements ListenableFutureCallback<SendResult<String, MessageLog>> {

	private final MessageLog message;

	@Override
	public void onSuccess(SendResult<String, MessageLog> result) {
		log.info("Mensaje enviado= [ " + this.message + " ] con offset=[ " + result.getRecordMetadata()
		        .offset() + " ]");
	}

	@Override
	public void onFailure(Throwable exception) {
		log.info("No se puede enviar el mensaje =[" + this.message + "] porque: " + exception.getMessage());
	}
}
