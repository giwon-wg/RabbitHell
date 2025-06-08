package com.example.rabbithell.domain.chat.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.rabbithell.domain.chat.dto.response.ChatMessageResponseDto;
import com.example.rabbithell.domain.chat.service.ChatRedisService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRedisController {

	private final ChatRedisService chatRedisService;

	@GetMapping("/{roomId}/history")
	public List<ChatMessageResponseDto> getChatHistory(@PathVariable String roomId) {
		return chatRedisService.getChatHistory(roomId);
	}
}
