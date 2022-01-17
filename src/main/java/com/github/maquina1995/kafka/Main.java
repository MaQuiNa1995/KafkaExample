package com.github.maquina1995.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.maquina1995.kafka.constants.KafkaConstants;
import com.github.maquina1995.kafka.messages.CustomMessage;
import com.github.maquina1995.kafka.service.ProducerMessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Main implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Main.class);
	}

	@Autowired
	private ProducerMessageService sendMessageService;

	@Override
	public void run(String... args) {
		this.sendString();
		this.sendPojo();
	}

	private void sendPojo() {
		CustomMessage customMessage = CustomMessage.builder()
		        .message("Mensaje asincrono contenido en un pojo")
		        .build();

		log.info("Envío de pojo asíncrono con topic: " + KafkaConstants.KAFKA_TOPIC_NAME_WITH_POJO);
		sendMessageService.sendAsynchronousPojoMessage(customMessage, KafkaConstants.KAFKA_TOPIC_NAME_WITH_POJO);
	}

	private void sendString() {
		log.info("Envío mensaje String asíncrono con topic: " + KafkaConstants.KAFKA_TOPIC_NAME);
		sendMessageService.sendAsynchronousStringMessage("Mensaje asincrono", KafkaConstants.KAFKA_TOPIC_NAME);

		log.info("Envío mensaje String asíncrono con topic: " + KafkaConstants.KAFKA_TOPIC_NAME_WITH_FILTER);
		sendMessageService.sendAsynchronousStringMessage("Mensaje asincrono con logica de filtrado",
		        KafkaConstants.KAFKA_TOPIC_NAME_WITH_FILTER);
	}

}
