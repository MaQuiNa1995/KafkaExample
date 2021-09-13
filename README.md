Para poder ejecutar este ejemplo debes montar una instancia de Zookeeper y kafka los pasos a seguir son los siguientes

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

Con el siguiente comando ejecutaremos el Zookeeper en windows: zookeeper-server-start.bat RUTA_KAFKA/config/zookeeper.properties

Para saber que el despligue se llevo a cabo perfectamente tendríamos que ver en la consola un mensaje parecido a este: INFO binding to port 0.0.0.0/0.0.0.0:2181

# Configuración Kafka
Como hicimos con el zookeeper tendremos que cambiar la property logs.dir del archivo de configuracion de kafka RUTA_KAFKA/config/server.properties que en mi caso está ubicado en C:\Programas\kafka_2.13-2.8.0\config\server.properties
Para que apunte a la carpeta que creamos anteriormente en mi caso: C:/Programas/kafka_2.13-2.8.0/data/kafka

Ten cuidado nuevamente con las barras al copiar de la barra del explorer tienes que usar las de la tecla 7 ( / )

# Ejecución Kafka
Con el siguiente comando ejecutaremos el Kafka en windows: kafka-server-start.bat RUTA_KAFKA/config/server.properties

Para ver si está bien desplegado tendremos qe fijarnos en la consola un mensaje parecido a este: INFO [KafkaServer id=0] started

Como dato adicional el servidor por defecto de kafka es: 9092

# Fuentes:

[Para hacer la guía](https://jd-bots.com/2021/08/14/start-zookeeper-and-kafka-in-windows-os/)

[Para hacer el proyecto](https://www.baeldung.com/spring-kafka)
