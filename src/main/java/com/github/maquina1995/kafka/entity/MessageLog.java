package com.github.maquina1995.kafka.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MessageLog {

	@Id
	@GeneratedValue(generator = "messageSequence",
	        strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "messageSequence",
	        allocationSize = 1)
	private Long id;
	private String message;
	@CreationTimestamp
	private LocalDateTime creationTime;

	public MessageLog(String message) {
		super();
		this.message = message;
	}

}
