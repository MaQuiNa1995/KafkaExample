package maquina1995.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;
import maquina1995.kafka.constants.KafkaConstants;
import maquina1995.kafka.service.SendMessageService;

@Slf4j
@SpringBootApplication
public class Main implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Main.class);
	}

	@Autowired
	private SendMessageService sendMessageService;

	@Override
	public void run(String... args) {

		log.info("Envío mensaje asíncrono con topic: " + KafkaConstants.KAFKA_TOPIC_NAME);
		sendMessageService.sendAsynchronousMessage("Mensaje asincrono", KafkaConstants.KAFKA_TOPIC_NAME);

		log.info("Envío mensaje asíncrono con topic: " + KafkaConstants.KAFKA_TOPIC_NAME_FILTER);
		sendMessageService.sendAsynchronousMessage("Mensaje asincrono con logica de filtrado",
		        KafkaConstants.KAFKA_TOPIC_NAME_FILTER);
	}

}
