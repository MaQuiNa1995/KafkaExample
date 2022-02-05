package com.github.maquina1995.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaConstants {

	// Topics
	private final String DEFAULT_TOPIC = "Maqui-Topic";

	public final String KAFKA_TOPIC_STRING_STRING = KafkaConstants.DEFAULT_TOPIC + "1";
	public final String KAFKA_TOPIC_STRING_STRING_WITH_FILTER = KafkaConstants.DEFAULT_TOPIC + "2";
	public final String KAFKA_TOPIC_STRING_POJO = KafkaConstants.DEFAULT_TOPIC + "3";

	// Groups
	public final String KAFKA_GROUP_ID = "Maqui-Group";
}
