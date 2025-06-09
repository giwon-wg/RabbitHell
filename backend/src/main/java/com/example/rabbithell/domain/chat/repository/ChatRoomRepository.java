package com.example.rabbithell.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.rabbithell.domain.chat.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
