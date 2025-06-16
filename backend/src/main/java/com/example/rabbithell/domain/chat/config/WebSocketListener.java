package com.example.rabbithell.domain.chat.config;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import com.example.rabbithell.domain.chat.dto.response.ChatMessageResponseDto;
import com.example.rabbithell.infrastructure.security.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketListener {

	private final SimpMessagingTemplate messagingTemplate;
	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, String> redisTemplate;

	@EventListener
	public void handleSessionConnected(SessionConnectedEvent event) {
		SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
		String sessionId = accessor.getSessionId();

		// ✅ cloverName을 JWT에서 반드시 추출
		String cloverName = getCloverNameFromToken(accessor);
		if (cloverName == null) {
			log.warn("⚠️ cloverName이 null입니다. 토큰에서 추출 실패!");
			cloverName = "익명";
		}

		String roomId = accessor.getFirstNativeHeader("roomId");
		if (roomId == null) roomId = "1";

		// Redis 저장
		redisTemplate.opsForValue().set("connected:room:" + roomId + ":" + cloverName, "1", Duration.ofMinutes(30));
		redisTemplate.opsForValue().set("session:" + sessionId, cloverName, Duration.ofMinutes(30));
		redisTemplate.opsForValue().set("session:room:" + sessionId, roomId, Duration.ofMinutes(30));

		// ✅ 로그 출력 시에도 cloverName 사용
		log.info("🔵 [입장] cloverName={}, sessionId={}, roomId={}", cloverName, sessionId, roomId);

		sendTotalUserCount(roomId);
	}


	@EventListener
	public void handleSessionDisconnect(SessionDisconnectEvent event) {
		String sessionId = event.getSessionId();
		String cloverName = redisTemplate.opsForValue().get("session:" + sessionId); // ✅ cloverName 그대로 사용
		String roomId = redisTemplate.opsForValue().get("session:room:" + sessionId);

		if (cloverName != null && roomId != null) {
			redisTemplate.delete("connected:room:" + roomId + ":" + cloverName);
			redisTemplate.delete("session:" + sessionId);
			redisTemplate.delete("session:room:" + sessionId);

			log.info("🔴 {}님이 퇴장했습니다. (sessionId: {}, roomId: {})", cloverName, sessionId, roomId);

			ChatMessageResponseDto quitMessage = ChatMessageResponseDto.createQuitMessage(cloverName);
			messagingTemplate.convertAndSend("/sub/chat/" + roomId, quitMessage);

			sendTotalUserCount(roomId);
		}
	}

	public int getCurrentUserCount(String roomId) {
		if (roomId == null) roomId = "1";
		Set<String> keys = redisTemplate.keys("connected:room:" + roomId + ":*");
		return keys != null ? keys.size() : 0;
	}

	private void sendTotalUserCount(String roomId) {
		Set<String> keys = redisTemplate.keys("connected:room:" + roomId + ":*");
		int userCount = keys != null ? keys.size() : 0;

		Map<String, Object> payload = Map.of("count", userCount);
		messagingTemplate.convertAndSend("/sub/user-count/" + roomId, payload);

		log.info("현재 채팅방 {}번 접속자 수: {}명", roomId, userCount);
	}

	@Scheduled(fixedRate = 60000)
	public void cleanExpiredSessions() {
		Set<String> keys = redisTemplate.keys("connected:room:*:*");
		if (keys != null) {
			for (String key : keys) {
				if (redisTemplate.getExpire(key) == -1) {
					redisTemplate.delete(key);
					log.info("🧹 TTL 없음 - 유령 세션 제거: {}", key);
				}
			}
		}
	}

	private String getCloverNameFromToken(SimpMessageHeaderAccessor accessor) {
		try {
			String authHeader = accessor.getFirstNativeHeader("Authorization");
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				String token = authHeader.substring(7);
				return jwtUtil.extractCloverName(token);
			}
		} catch (Exception e) {
			log.warn("토큰에서 cloverName 추출 실패: {}", e.getMessage());
		}
		return null;
	}
}
