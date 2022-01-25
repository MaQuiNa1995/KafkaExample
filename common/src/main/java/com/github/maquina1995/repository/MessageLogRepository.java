package com.github.maquina1995.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.maquina1995.entity.MessageLog;

public interface MessageLogRepository extends JpaRepository<MessageLog, Long> {

	Optional<MessageLog> findByMessage(String message);

}
