Para poder ejecutar este ejemplo debes montar una instancia de Zookeeper y kafka los pasos a seguir son los siguientes

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/1021dba8f6eb4a0694405956c43878a6)](https://www.codacy.com/gh/MaQuiNa1995/KafkaExample/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=MaQuiNa1995/KafkaExample&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/1021dba8f6eb4a0694405956c43878a6)](https://www.codacy.com/gh/MaQuiNa1995/KafkaExample/dashboard?utm_source=github.com&utm_medium=referral&utm_content=MaQuiNa1995/KafkaExample&utm_campaign=Badge_Coverage)

# Diagrama Uml del proyecto

![alt text](https://github.com/MaQuiNa1995/KafkaExample/blob/master/diagramaUml.svg?raw=true)

# Docker

Si quieres usar docker y no hacer la instalación en tu máquina puedes usar este dockerfile para a traves de docker montar un kafka y zookeeper sino digue leyendo :)

```
version: '2'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - 22181:2181
  
  kafka:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    ports:
      - 29092:29092
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

# Descarga
Lo primero que tenemos que hacer es descargar kafka desde este enlace: https://dlcdn.apache.org/kafka/2.8.0/kafka_2.13-2.8.0.tgz (Asegúrate de que sea la última versión :) )

# Descompresión
Con el programa open source 7Zip podremos descomprimirlo , os recomiendo hacerlo en una carpeta donde la ruta resultante no sea demasiado larga, ya que esto podría dar problemas. Tambien podrías tener problemas si tu variable %JAVA_HOME% tiene espacios en blanco tenlo en cuenta

A partir de este momento haremos referencia a la ruta de instalación de kafka como RUTA_KAFKA (Ejemplo: C:\Programas\kafka_2.13-2.8.0)

# Preparación del sistema de archivos
Para tener un poco organizado el sistema de archivos, vamos a crear en RUTA_KAFKA las siguientes carpetas:

	 RUTA_KAFKA

		|-> data
	
			|-> kafka
		
			|-> zookeeper

# Configuración Zookeeper
Como adelante antes debes tener java instalado y configurado (Variables de entorno) sin que esta ruta tenga espacios en blanco

Para configurar el zookeeper para que genere los log en la carpeta creada en el paso anterior tendremos que ir al archivo ubicado en RUTA_KAFKA/config/zookeeper.properties
Tendremos que cambiar la property llamada dataDir por la ruta de la carpeta anteriormente creada en mi caso (C:/Programas/kafka_2.13-2.8.0/data/zookeeper)

Ten cuidado con las barras al copiar de la barra del explorer tienes que usar las de la tecla 7 ( / )

# Ejecución Zookeeper
Para ejecutar zookeeper podemos añadir la ruta RUTA_KAFKA/bin/windows a nuestro PATH para mayor comodidad

Con el siguiente comando ejecutaremos el Zookeeper en windows: zookeeper-server-start.bat ../../config/zookeeper.properties

Para saber que el despligue se llevo a cabo perfectamente tendríamos que ver en la consola un mensaje parecido a este: INFO binding to port 0.0.0.0/0.0.0.0:2181

# Configuración Kafka
Como hicimos con el zookeeper tendremos que cambiar la property logs.dir del archivo de configuracion de kafka RUTA_KAFKA/config/server.properties que en mi caso está ubicado en C:\Programas\kafka_2.13-2.8.0\config\server.properties
Para que apunte a la carpeta que creamos anteriormente en mi caso: C:/Programas/kafka_2.13-2.8.0/data/kafka

Ten cuidado nuevamente con las barras al copiar de la barra del explorer tienes que usar las de la tecla 7 ( / )

# Ejecución Kafka
Con el siguiente comando ejecutaremos el Kafka en windows: kafka-server-start.bat ../../config/server.properties

Para ver si está bien desplegado tendremos qe fijarnos en la consola un mensaje parecido a este: INFO [KafkaServer id=0] started

Como dato adicional el servidor por defecto de kafka es: 9092

# Testing básico de kafka

En el proyecto os he dejado una clase que a traves de embedded kafka prueba un consumidor y productor básicos creados en el propio tests
```
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import com.github.maquina1995.producer.Main;

/**
 * <b>Lecciones Aprendidas:</b> Aquí debemos usar el puerto que tenemos
 * configurado en nuestro properties para que asi sea igual el del embebed kafka
 * y el que tendríamos configurado por la property
 * 
 * @author MaQuiNa1995
 *
 */
@EmbeddedKafka(partitions = 1, ports = 9092, topics = SimpleKafkaTest.TOPIC)
@SpringBootTest(classes = { Main.class })
class SimpleKafkaTest {

	/**
	 * Lo ponemos protected para poder usarlo en la anotación @EmbeddedKafka en la
	 * línea 34
	 */
	protected static final String TOPIC = "prueba";
	private static final String KAFKA_GROUP = "testGroup";

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	@Test
	void testReceivingKafkaEvents() {

		embeddedKafkaBroker.getBrokerAddresses();

		try (Consumer<Integer, String> consumer = this.configureConsumer();
				Producer<Integer, String> producer = this.configureProducer()) {

			producer.send(new ProducerRecord<>(TOPIC, 123, "my-test-value"));

			ConsumerRecord<Integer, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer, TOPIC);
			Assertions.assertNotNull(singleRecord);
			Assertions.assertEquals(singleRecord.key(), 123);
			Assertions.assertEquals(singleRecord.value(), "my-test-value");
		}
	}

	private Consumer<Integer, String> configureConsumer() {
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(KAFKA_GROUP, "true", embeddedKafkaBroker);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		Consumer<Integer, String> consumer = new DefaultKafkaConsumerFactory<Integer, String>(consumerProps)
				.createConsumer();

		consumer.subscribe(List.of(TOPIC));
		return consumer;
	}

	private Producer<Integer, String> configureProducer() {
		Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
		return new DefaultKafkaProducerFactory<Integer, String>(producerProps).createProducer();
	}
}
```

# Fuentes:

[Para hacer la guía](https://jd-bots.com/2021/08/14/start-zookeeper-and-kafka-in-windows-os/)

[Para hacer el proyecto](https://www.baeldung.com/spring-kafka)

Imagen creada con: https://github.com/MaQuiNa1995/ExtractorUml
