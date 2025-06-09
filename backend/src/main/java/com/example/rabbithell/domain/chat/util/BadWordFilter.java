package com.example.rabbithell.domain.chat.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.rabbithell.domain.chat.exception.ChatMessageException;
import com.example.rabbithell.domain.chat.exception.ChatMessageExceptionCode;

public class BadWordFilter {

	//문자열 내부에 포함된 욕 단어단위 아님
	public static final Set<String> badWords = new HashSet<>(
		Arrays.asList("시발", "개새끼")
	);

	public static String filter(String message) {
		if (message == null) {
			throw new ChatMessageException(ChatMessageExceptionCode.INVALID_PAYLOAD);
		}

		String filteredMessage = message;

		for (String badWord : badWords) {
			Pattern pattern = Pattern.compile(badWord, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(filteredMessage);

			StringBuilder sb = new StringBuilder();
			int lastEnd = 0;

			while (matcher.find()) {
				sb.append(filteredMessage, lastEnd, matcher.start());
				sb.append("*".repeat(matcher.end() - matcher.start()));
				lastEnd = matcher.end();
			}
			sb.append(filteredMessage.substring(lastEnd));
			filteredMessage = sb.toString();
		}

		return filteredMessage;
	}
}
