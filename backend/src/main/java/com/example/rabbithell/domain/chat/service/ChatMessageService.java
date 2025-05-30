package com.example.rabbithell.domain.chat.service;

import com.example.rabbithell.domain.user.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

@Service
public interface ChatMessageService {
	void saveMessage(Long roomId, User user, @NotBlank(message = "내용은 반드시 작성되어야 합니다.") String message);
}
