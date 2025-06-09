package com.example.rabbithell.domain.user.service;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import com.example.rabbithell.domain.chat.exception.ChatMessageException;
import com.example.rabbithell.domain.chat.exception.ChatMessageExceptionCode;
import com.example.rabbithell.domain.user.model.User;
import com.example.rabbithell.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    //토큰이 아닌 user로 객체를 받아오기 위함
    public User getUserFromAccessor(SimpMessageHeaderAccessor accessor) {
        Object userIdObj = accessor.getSessionAttributes().get("userId");
        if (userIdObj == null) {
            throw new ChatMessageException(ChatMessageExceptionCode.UNAUTHORIZED_ACCESS);
        }

        Long userId = Long.valueOf(userIdObj.toString());

        return userRepository.findById(userId)
                .orElseThrow(() -> new ChatMessageException(ChatMessageExceptionCode.UNAUTHORIZED_ACCESS));
    }

}
