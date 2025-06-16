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
@Table(name = "chat_message")
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	// ✅ 연관관계 매핑 (user_id 외래키만 DB에 들어감)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_room_id", nullable = false)
	private ChatRoom chatRoom;

	// ✅ 실제 메시지 본문
	@Column(nullable = false)
	private String contents;

	// ✅ 채팅 당시의 clovername (JWT에서 추출된 값 저장)
	@Column(name = "clovername", nullable = false)
	private String clovername;

	@CreatedDate
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private boolean isDeleted = false;

	// ✅ soft delete 처리
	public void softDelete() {
		this.isDeleted = true;
	}
}
