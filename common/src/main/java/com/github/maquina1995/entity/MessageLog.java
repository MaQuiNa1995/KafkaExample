package com.github.maquina1995.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * {@link NoArgsConstructor} Necesario para la deserialización de kafka
 */
@Getter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageLog {

	private String message;
	@Default
	private LocalDateTime creationTime = LocalDateTime.now();

}
