package com.montreal.chat.util;

import static com.montreal.chat.common.Constants.LINE_SEPARATOR;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
	private Utils() {
		
	}
	public static Map<String, String> stringToMap(String str) {
		return Stream.of(str.split(LINE_SEPARATOR))
		.map(elem -> elem.split("="))
        .filter(elem -> elem.length==2)
        .collect(Collectors.toMap(e -> e[0].trim(), e -> e[1].trim()));
	}

	public static String mapToString(Map<String, String> map) {
		return map.entrySet().stream()
				.map(entry -> entry.getKey() + "=" + entry.getValue())
				.collect(Collectors.joining(LINE_SEPARATOR));
	}
}
