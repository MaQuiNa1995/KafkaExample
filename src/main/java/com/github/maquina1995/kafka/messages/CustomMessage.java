package com.github.maquina1995.kafka.messages;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Pojo usado para envío de mensaje en Kafka
 * <p>
 * El encargado de la de-serialización como siempre, es Jackson por defecto
 * <p>
 * <b>Lecciones Aprendidas:</b> Se debe tener {@link Getter} y
 * {@link NoArgsConstructor} Al menos para que la clase pueda ser de-serializada
 * <p>
 * No olvidarse de {@link Serializable}
 * 
 * @author MaQuiNa1995
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames = false)
public class CustomMessage implements Serializable {

	private String message;
	@Builder.Default
	private String creationTime = LocalDateTime.now()
	        .toString();

}
