package com.example.rabbithell.domain.chat.entity;

import java.util.List;
import jakarta.persistence.*;

import com.example.rabbithell.domain.user.model.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "chat_room")
public class ChatRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roomId;

	@Column(nullable = false)
	private String chatRoomName;

	@OneToMany(mappedBy = "chatRoom", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChatMessage> messageList;


	public ChatRoom(String chatRoomName) {
		this.chatRoomName = chatRoomName;
	}

}
