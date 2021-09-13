package maquina1995.kafka.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaConstants {

	// Topics
	private final String DEFAULT_TOPIC = "Maqui-Topic";

	public final String KAFKA_TOPIC_NAME = KafkaConstants.DEFAULT_TOPIC + "1";
	public final String KAFKA_TOPIC_NAME_WITH_FILTER = KafkaConstants.DEFAULT_TOPIC + "2";
	public final String KAFKA_TOPIC_NAME_WITH_POJO = KafkaConstants.DEFAULT_TOPIC + "3";

	// Groups
	public final String KAFKA_GROUP_ID = "Maqui-Group";
}
