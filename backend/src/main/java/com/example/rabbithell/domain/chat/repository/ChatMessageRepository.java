package com.example.rabbithell.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.chat.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
}
