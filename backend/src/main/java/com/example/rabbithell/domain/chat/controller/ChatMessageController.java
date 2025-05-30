package com.example.rabbithell.domain.chat.controller;

import com.example.rabbithell.domain.chat.dto.ChatMessageDto;
import com.example.rabbithell.domain.chat.dto.MessageType;
import com.example.rabbithell.domain.chat.exception.ChatMessageException;
import com.example.rabbithell.domain.chat.exception.ChatMessageExceptionCode;
import com.example.rabbithell.domain.chat.service.ChatMessageService;
import com.example.rabbithell.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import static com.example.rabbithell.domain.chat.dto.MessageType.*;

@RequiredArgsConstructor
@Controller
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{roomId}")
    public void sendChatMessage(
        @DestinationVariable Long roomId,
        ChatMessageDto messageDto,
        @AuthenticationPrincipal User user
    ) {
        if (user == null) {
            throw new ChatMessageException(ChatMessageExceptionCode.MESSAGE_PROCESSING_ERROR);
        }

        if (messageDto == null || messageDto.getMessageType() == null) {
            throw new ChatMessageException(ChatMessageExceptionCode.INVALID_PAYLOAD);
        }

        String username = user.getName();

        try {
            switch (messageDto.getMessageType()) {
                case ENTER -> messageDto.setMessage(username + "님이 입장하셨습니다.");
                case CHAT -> {
                    if (!StringUtils.hasText(messageDto.getMessage())) {
                        throw new ChatMessageException(ChatMessageExceptionCode.NULL_MESSAGE);
                    }
                    chatMessageService.saveMessage(roomId, user, messageDto.getMessage());
                }
                case QUIT -> messageDto.setMessage(username + "님이 퇴장하셨습니다.");
                default -> throw new ChatMessageException(ChatMessageExceptionCode.INVALID_PAYLOAD);
            }

            messageDto.setChatUserId(user.getId());
            messageDto.setUsername(username);
            messagingTemplate.convertAndSend("/sub/room/" + roomId, messageDto);

        } catch (ChatMessageException e) {
            throw e;
        } catch (Exception e) {
            throw new ChatMessageException(ChatMessageExceptionCode.MESSAGE_PROCESSING_ERROR);
        }
    }
}
