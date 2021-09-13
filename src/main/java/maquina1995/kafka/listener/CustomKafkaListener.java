package maquina1995.kafka.listener;

import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Esta clase representa un comportamiento custom para manejar los resultados
 * asincronos de los mensajes que se envian a kafka
 * <p>
 * El constructor es por conveniencia mía para imprimir el mensaje por consola a
 * modo ejemplo no es obligatorio ni mucho menos
 * <p>
 * Los métodos se ejecutan según:
 * <li>Éxito: {@link CustomKafkaListener#onSuccess(SendResult)}</li>
 * <li>Problema: {@link CustomKafkaListener#onFailure(Throwable)}</li>
 * <p>
 * 
 * @author MaQuiNa1995
 */
public class CustomKafkaListener implements ListenableFutureCallback<SendResult<String, String>> {

	private final String message;

	public CustomKafkaListener(String message) {
		super();
		this.message = message;
	}

	@Override
	public void onSuccess(SendResult<String, String> result) {
		System.out.println("Mensaje enviado= [ " + this.message + " ] con offset=[ " + result.getRecordMetadata()
		        .offset() + " ]");
	}

	@Override
	public void onFailure(Throwable ex) {
		System.out.println("No se puede enviar el mensaje =[" + this.message + "] porque: " + ex.getMessage());
	}
}
