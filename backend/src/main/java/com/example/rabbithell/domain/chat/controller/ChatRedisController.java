package com.example.rabbithell.domain.chat.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.rabbithell.domain.chat.dto.response.ChatMessageResponseDto;
import com.example.rabbithell.domain.chat.service.ChatRedisService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRedisController {

	private final ChatRedisService chatRedisService;

	//탭 전환시 대화 기록 유지 위함
	@GetMapping("/{roomId}/history")
	public List<ChatMessageResponseDto> getChatHistory(@PathVariable String roomId) {
		return chatRedisService.getChatHistory(roomId);
	}
}
