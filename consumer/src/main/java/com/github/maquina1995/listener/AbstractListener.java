package com.github.maquina1995.listener;

import java.util.concurrent.CountDownLatch;

import com.github.maquina1995.constants.KafkaConstants;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class AbstractListener {

	protected CountDownLatch latch = new CountDownLatch(10);
	protected String payload = null;

	protected void processMessage(int listenerNumber, String message, String topic) {

		this.payload = message;
		this.latch.countDown();

		log.info("[Listener-" + listenerNumber + "] Mensaje recibido con grupo: " + KafkaConstants.KAFKA_GROUP_ID
				+ " Topic: " + topic + " Mensaje: " + message);
	}
}
