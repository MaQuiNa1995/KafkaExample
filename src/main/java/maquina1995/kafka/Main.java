package maquina1995.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import maquina1995.kafka.service.SendMessageService;

@SpringBootApplication
public class Main implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Main.class);
	}

	@Autowired
	private SendMessageService sendMessageService;

	@Override
	public void run(String... args) {
		System.out.println("Inicio Aplicacion");

		sendMessageService.sendAsynchronousMessage("Mensaje asincrono :)");

		System.out.println("Fin Aplicacion");
	}

}
