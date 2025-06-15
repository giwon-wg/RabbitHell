package com.example.rabbithell.domain.chat.common;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ChatConstants {

	public static final class Chat {
		public static final long MESSAGE_COOLDOWN_MS = 1000; // 1초
		public static final int MAX_MESSAGE_LENGTH = 500;
		public static final int RECENT_MESSAGES_MINUTES = 10;
	}

	public static final class Redis {
		public static final String CHAT_HISTORY_PREFIX = "chat:history:";
		public static final String CHAT_CACHE_PREFIX = "chat:cache:";
		public static final String USER_SESSION_PREFIX = "user:session:";
		public static final String ROOM_USERS_PREFIX = "room:users:";
		public static final String COOLDOWN_PREFIX = "chat:cooldown:";
	}
}
