package com.example.rabbithell.domain.chat.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import com.example.rabbithell.domain.user.model.User;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="chat_message")
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_room_id", nullable = false)
	private ChatRoom chatRoom;

	@Column(nullable = false)
	private String contents;

	@CreatedDate
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private boolean isDeleted = false;

	// 소프트 딜리트 메서드
	public void softDelete() {
		this.isDeleted = true;
	}

}
