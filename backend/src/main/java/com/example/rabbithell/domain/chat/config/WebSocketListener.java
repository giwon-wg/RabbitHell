// WebSocketListener.java
package com.example.rabbithell.domain.chat.config;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.example.rabbithell.domain.chat.dto.response.ChatMessageResponseDto;
import com.example.rabbithell.infrastructure.security.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.example.rabbithell.domain.chat.common.Constants.Chat.GLOBAL_ROOM_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketListener {
	private final SimpMessagingTemplate messagingTemplate;
	private final JwtUtil jwtUtil;

	private final ConcurrentHashMap<String, String> sessionUserMap = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, String> sessionRoomMap = new ConcurrentHashMap<>();

	@EventListener
	public void handleSessionConnected(SessionConnectedEvent event) {
		SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
		Principal user = accessor.getUser();
		String sessionId = accessor.getSessionId();

		// JWT에서 cloverName 추출
		String username = getCloverNameFromToken(accessor);
		if (username == null) {
			username = user != null ? user.getName() : "익명"; // fallback with null check
		}

		log.info("🔵 {}님이 입장했습니다", username);

		String roomId = null;
		if (accessor.getSessionAttributes() != null) {
			roomId = (String) accessor.getSessionAttributes().get("roomId");
		}

		// roomId가 null이면 기본값 "1"로 설정
		if (roomId == null) {
			roomId = "1";
			log.info("🔵 roomId가 null이어서 기본값 '1'로 설정했습니다.");
		}

		// 세션 정보 저장 (user가 null이어도 저장)
		sessionUserMap.put(sessionId, username);
		sessionRoomMap.put(sessionId, roomId);

		log.info("🔵 {}님이 입장했습니다. (sessionId: {}, roomId: {})", username, sessionId, roomId);

		// 입장 메시지 전송
		ChatMessageResponseDto enterMessage = ChatMessageResponseDto.createEnterMessage(username);
		messagingTemplate.convertAndSend("/sub/chat/" + roomId, enterMessage);

		// roomId가 "1"인 경우 접속자 수 전송
		if ("1".equals(roomId)) {
			sendTotalUserCount();
		}
	}

	private String getCloverNameFromToken(SimpMessageHeaderAccessor accessor) {
		try {
			// Authorization 헤더에서 토큰 추출
			String authHeader = accessor.getFirstNativeHeader("Authorization");
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				String token = authHeader.substring(7);
				return jwtUtil.extractCloverName(token); // 이미 구현된 메서드 사용
			}
		} catch (Exception e) {
			log.warn("토큰에서 cloverName 추출 실패: {}", e.getMessage());
		}
		return null;
	}

	@EventListener
	public void handleSessionDisconnect(SessionDisconnectEvent event) {
		String sessionId = event.getSessionId();
		String username = sessionUserMap.remove(sessionId);
		String roomId = sessionRoomMap.remove(sessionId);

		// null 체크 후 기본값 설정
		if (username == null) {
			username = "익명";
		}
		if (roomId == null) {
			roomId = "1";
		}

		log.info("🔴 {}님이 퇴장했습니다. (sessionId: {}, roomId: {})", username, sessionId, roomId);

		// 퇴장 메시지 전송
		ChatMessageResponseDto quitMessage = ChatMessageResponseDto.createQuitMessage(username);
		messagingTemplate.convertAndSend("/sub/chat/" + roomId, quitMessage);

		// roomId가 "1"인 경우 접속자 수 전송
		if ("1".equals(roomId)) {
			sendTotalUserCount();
		}
	}

	private void sendTotalUserCount() {
		long count = sessionRoomMap.values().stream()
			.filter(id -> id != null && "1".equals(id))
			.count();

		Map<String, Object> payload = Map.of("count", count);
		messagingTemplate.convertAndSend("/sub/user-count/1", payload);

		log.info("📊 현재 채팅방 1번 접속자 수: {}", count);
	}

	// 초기 접속자 수를 가져오는 REST API용 메서드 (Controller에서 호출 가능)
	public long getCurrentUserCount(String roomId) {
		if (roomId == null) {
			roomId = "1";
		}

		final String targetRoomId = roomId;
		long count = sessionRoomMap.values().stream()
			.filter(id -> id != null && targetRoomId.equals(id))
			.count();

		log.info("📊 채팅방 {}번 현재 접속자 수 조회: {}", roomId, count);
		return count;
	}
}
