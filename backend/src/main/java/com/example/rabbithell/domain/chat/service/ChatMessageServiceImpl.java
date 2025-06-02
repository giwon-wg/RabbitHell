package com.example.rabbithell.domain.chat.service;

import com.example.rabbithell.domain.chat.dto.request.ChatMessageRequestDto;
import com.example.rabbithell.domain.chat.dto.response.ChatMessageResponseDto;
import com.example.rabbithell.domain.chat.entity.ChatMessage;
import com.example.rabbithell.domain.chat.exception.ChatMessageException;
import com.example.rabbithell.domain.chat.exception.ChatMessageExceptionCode;
import com.example.rabbithell.domain.chat.repository.ChatMessageRepository;
import com.example.rabbithell.domain.chat.util.BadWordFilter;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.rabbithell.domain.chat.common.Constants.Chat.MESSAGE_COOLDOWN_MS;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

	private final ChatMessageRepository chatMessageRepository;
	private final UserRepository userRepository;

	private final SimpMessagingTemplate messagingTemplate;
	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public void saveMessage(Long userId, String message) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("사용자 없음"));

		// 욕설 필터링 적용
		String filteredMessage = BadWordFilter.filter(message);

		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setUser(user);
		chatMessage.setContents(filteredMessage);
		chatMessage.setCreatedAt(LocalDateTime.now());

		chatMessageRepository.save(chatMessage);
	}


	@Override
	public void deleteMessage(Long roomId, Long chatMessageId) {
		ChatMessage message = chatMessageRepository.findById(chatMessageId)
			.orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));

		message.softDelete();
		chatMessageRepository.save(message); // 삭제 플래그만 갱신
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

	@Override
	public void sendAdminMessage(Long roomId, ChatMessageResponseDto dto, User adminUser) {
		validateAdmin(adminUser);

		ChatMessageResponseDto adminMessage = ChatMessageResponseDto.createAdminMessage(
			adminUser.getId(),
			"\uD83D\uDEA8 [공지사항] " + dto.message()
		);

		messagingTemplate.convertAndSend("/sub/room/" + roomId, adminMessage);
	}

	//관리자 권한 메소드
	private void validateAdmin(User user) {
		if (user == null || !user.getRole().equals("ADMIN")) {
			throw new ChatMessageException(ChatMessageExceptionCode.NO_ADMIN_PRIVILEGES);
		}
	}

}
