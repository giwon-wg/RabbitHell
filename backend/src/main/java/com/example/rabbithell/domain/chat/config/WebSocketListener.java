package com.example.rabbithell.domain.chat.config;

import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import com.example.rabbithell.domain.chat.dto.response.ChatMessageResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static com.example.rabbithell.domain.chat.common.Constants.Chat.GLOBAL_ROOM_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketListener {
	private final SimpMessagingTemplate messagingTemplate;

	private final ConcurrentHashMap<String, String> sessionUserMap = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, String> sessionRoomMap = new ConcurrentHashMap<>();

	//웹소켓 연결시
	@EventListener
	public void handleSessionConnected(SessionConnectedEvent event) {
		Principal user = event.getUser();
		String sessionId = SimpMessageHeaderAccessor.wrap(event.getMessage()).getSessionId();
		String roomId = SimpMessageHeaderAccessor.wrap(event.getMessage()).getFirstNativeHeader("roomId");

		if (user != null && roomId != null) {
			String username = user.getName();
			sessionUserMap.put(sessionId, username);
			sessionRoomMap.put(sessionId, roomId);

			log.info("🔵 {}님이 입장했습니다. (sessionId: {}, roomId: {})", username, sessionId, roomId);

			// 입장 메시지 전송
			ChatMessageResponseDto enterMessage = ChatMessageResponseDto.createEnterMessage(username);
			messagingTemplate.convertAndSend("/sub/chat/" + roomId, enterMessage);

			// 전체 채팅방(roomId = 0)인 경우 접속자 수 업데이트
			if ("0".equals(roomId)) {
				sendTotalUserCount();
			}
		}
	}

	//웹소켓 연결 해제시
	@EventListener
	public void handleSessionDisconnect(SessionDisconnectEvent event) {
		String sessionId = event.getSessionId();
		String username = sessionUserMap.remove(sessionId);
		String roomId = sessionRoomMap.remove(sessionId);

		if (username != null && roomId != null) {
			log.info("🔴 {}님이 퇴장했습니다. (sessionId: {}, roomId: {})", username, sessionId, roomId);

			// 퇴장 메시지 전송
			ChatMessageResponseDto quitMessage = ChatMessageResponseDto.createQuitMessage(username);
			messagingTemplate.convertAndSend("/sub/chat/" + roomId, quitMessage);

			// 전체 채팅방(roomId = 0)인 경우 접속자 수 업데이트
			if ("0".equals(roomId)) {
				sendTotalUserCount();
			}
		}
	}

	/**
	 * 전체 채팅방 접속자 수 조회
	 */
	public long getTotalUserCount() {
		return sessionRoomMap.values().stream()
				.filter(roomId -> "0".equals(roomId))
				.count();
	}

	/**
	 * 전체 채팅방 접속자 수 업데이트 메시지 전송
	 * 수정된 부분: getTotalUserCount() 메서드 사용
	 */
	private void sendTotalUserCount() {
		long count = getTotalUserCount(); // 수정: 실제 접속자 수 조회
		log.info("📊 전체 채팅방 접속자 수: {}", count);
		messagingTemplate.convertAndSend("/sub/chat/" + GLOBAL_ROOM_ID,
				ChatMessageResponseDto.system(count, "접속자 수 갱신"));
	}

	/**
	 * 특정 방의 접속자 수 조회
	 */
	public long getUserCountByRoom(String roomId) {
		return sessionRoomMap.values().stream()
				.filter(id -> roomId.equals(id))
				.count();
	}

	/**
	 * 전체 접속자 목록 조회 (관리자용)
	 */
	public ConcurrentHashMap<String, String> getAllConnectedUsers() {
		return new ConcurrentHashMap<>(sessionUserMap);
	}
}
