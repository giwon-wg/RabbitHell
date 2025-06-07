package com.example.rabbithell.domain.chat.service;

import com.example.rabbithell.domain.chat.dto.request.ChatMessageRequestDto;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

import static com.example.rabbithell.domain.chat.common.Constants.Chat.MESSAGE_COOLDOWN_MS;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

	private final ChatMessageRepository chatMessageRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;

	private final SimpMessagingTemplate messagingTemplate;
	private final RedisTemplate<String, String> redisTemplate;

	//사용자
	@Override
	public ChatMessage saveMessage(Long roomId, Long userId, String message) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("사용자 없음"));

		ChatRoom chatRoom = chatRoomRepository.findById(roomId)
			.orElseThrow(() -> new RuntimeException("채팅방 없음"));
		log.info("인증된 사용자: ID={}, Username={}", userId,user.getName());
		// 욕설 필터링 적용
		String filteredMessage = BadWordFilter.filter(message);

		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setUser(user);
		chatMessage.setChatRoom(chatRoom); // ✅ 반드시 설정
		chatMessage.setContents(filteredMessage);
		chatMessage.setCreatedAt(LocalDateTime.now());

		chatMessageRepository.save(chatMessage);

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
