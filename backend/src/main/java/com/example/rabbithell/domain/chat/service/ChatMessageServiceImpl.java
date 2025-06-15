package com.example.rabbithell.domain.chat.service;

import java.time.LocalDateTime;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.example.rabbithell.domain.chat.config.messagebroker.RedisPublisher;
import com.example.rabbithell.domain.chat.dto.response.ChatMessageResponseDto;
import com.example.rabbithell.domain.chat.entity.ChatMessage;
import com.example.rabbithell.domain.chat.entity.ChatRoom;
import com.example.rabbithell.domain.chat.exception.ChatMessageException;
import com.example.rabbithell.domain.chat.exception.ChatMessageExceptionCode;
import com.example.rabbithell.domain.chat.repository.ChatMessageRepository;
import com.example.rabbithell.domain.chat.repository.ChatRoomRepository;
import com.example.rabbithell.domain.chat.util.BadWordFilter;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;
import com.example.rabbithell.domain.chat.internal.ChatRedisWriter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static com.example.rabbithell.domain.chat.common.ChatConstants.Chat.MESSAGE_COOLDOWN_MS;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

	private final ChatMessageRepository chatMessageRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;

	private final SimpMessagingTemplate messagingTemplate;
	private final RedisTemplate<String, String> redisTemplate;

	private final RedisPublisher redisPublisher;
	private final ChatRedisWriter chatRedisWriter;


	//사용자
	public ChatMessage saveMessage(Long roomId, Long userId, String cloverName, String message) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("사용자 없음"));

		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new RuntimeException("채팅방 없음"));

		String filtered = BadWordFilter.filter(message);

		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setUser(user); // 🔗 연관관계 매핑 (user_id)
		chatMessage.setChatRoom(chatRoom); // 🔗 연관관계 매핑 (chat_room_id)
		chatMessage.setContents(filtered); // 🔥 욕설 필터링된 메시지 저장
		chatMessage.setClovername(cloverName); // ✅ JWT에서 추출한 clovername 저장
		chatMessage.setCreatedAt(LocalDateTime.now());
		chatMessage.setDeleted(false);

		chatMessageRepository.save(chatMessage);

		// ✅ Redis 저장 및 발행은 Service 내부에서!
		ChatMessageResponseDto dto = ChatMessageResponseDto.createChatMessage(cloverName, chatMessage);
		chatRedisWriter.saveChatMessage(roomId.toString(), dto);
		redisPublisher.publish(roomId, dto);

		return chatMessage;
	}



	@Override
	public void isOnCooldown(Long roomId, Long userId) {
		String key = "chat:cooldown:" + roomId + ":" + userId;

		String lastTimeStr = redisTemplate.opsForValue().get(key);

		if (lastTimeStr != null) {
			long lastTime = Long.parseLong(lastTimeStr);
			if (System.currentTimeMillis() - lastTime < MESSAGE_COOLDOWN_MS) {
				throw new ChatMessageException(ChatMessageExceptionCode.COOLDOWN_NOT_FINISHED); // 예외는 추가 필요
			}
		}

		redisTemplate.opsForValue().set(key, String.valueOf(System.currentTimeMillis()));

	}

	//관리자
	@Override
	public void sendAdminMessage(Long roomId, ChatMessageResponseDto dto, User adminUser) {
		validateAdmin(adminUser);

		ChatMessageResponseDto adminMessage = ChatMessageResponseDto.createAdminMessage(
			adminUser.getId(),
			"\uD83D\uDEA8 [공지사항] " + dto.message()
		);

		messagingTemplate.convertAndSend("/sub/room/" + roomId, adminMessage);
	}



	@Override
	public void sendMessageDeletedNotification(Long roomId, Long messageId, User admin) {

		validateAdmin(admin);
		ChatMessage message = chatMessageRepository.findById(messageId)
			.orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));

		message.softDelete();
		chatMessageRepository.save(message); // 삭제 플래그만 갱신
	}


	//관리자 권한 메소드
	private void validateAdmin(User user) {
		if (user == null || !user.getRole().equals("ADMIN")) {
			throw new ChatMessageException(ChatMessageExceptionCode.UNAUTHORIZED_ACCESS);
		}
	}

}
