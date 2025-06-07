// UserCountController.java
package com.example.rabbithell.domain.chat.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.domain.chat.config.WebSocketListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class UserCountController {

	private final WebSocketListener webSocketListener;

	// 특정 채팅방의 현재 접속자 수를 조회하는 API
	@GetMapping("/rooms/{roomId}/user-count")
	public ResponseEntity<Map<String, Object>> getUserCount(@PathVariable String roomId) {
		try {
			long count = webSocketListener.getCurrentUserCount(roomId);

			Map<String, Object> response = Map.of(
				"success", true,
				"count", count,
				"roomId", roomId
			);

			log.info(" 채팅방 {} 접속자 수 API 응답: {}", roomId, count);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.error("접속자 수 조회 중 오류 발생: {}", e.getMessage());

			Map<String, Object> errorResponse = Map.of(
				"success", false,
				"count", 0L,
				"roomId", roomId,
				"error", "접속자 수 조회 실패"
			);

			return ResponseEntity.ok(errorResponse); // 에러여도 200으로 반환하여 프론트에서 처리하기 쉽게
		}
	}

	//기본 채팅방 접속자 수 조회
	@GetMapping("/user-count")
	public ResponseEntity<Map<String, Object>> getDefaultRoomUserCount() {
		return getUserCount("1");
	}
}
