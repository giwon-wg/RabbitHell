package com.example.rabbithell.domain.chat.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.example.rabbithell.domain.chat.exception.ChatMessageException;
import com.example.rabbithell.domain.chat.exception.ChatMessageExceptionCode;

public class BadWordFilter {

	public static final Set<String> badWords = new HashSet<>(
		Arrays.asList("시발", "개새끼")
	);

	public static String filter(String message) {
		if (message == null) {
			throw new ChatMessageException(ChatMessageExceptionCode.INVALID_PAYLOAD);
		}
		String[] words = message.split("\\s+");
		StringBuilder filteredMessage = new StringBuilder();

		for (String word : words) {
			if (badWords.contains(word.toLowerCase())) {
				filteredMessage.append("***");
			} else {
				filteredMessage.append(word).append(" ");
			}
		}
		return filteredMessage.toString().trim();
	}
}
