package com.example.rabbithell.domain.user.service;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import com.example.rabbithell.domain.user.model.User;

public interface UserService {

	User getUserFromAccessor(SimpMessageHeaderAccessor accessor);

}
