package troublog.backend.global.common.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonConverter {

	public static final String REPLACEMENT = "";

	public String toJson(List<Integer> scores) {
		if (scores == null || scores.isEmpty()) {
			return "[]";
		}
		return scores.stream()
			.map(String::valueOf)
			.collect(Collectors.joining(",", "[", "]"));
	}

	public List<Integer> toList(String json) {
		if (json == null || json.trim().equals("[]") || json.trim().isEmpty()) {
			return Collections.emptyList();
		}

		String content = json.replace("[", REPLACEMENT).replace("]", REPLACEMENT).trim();
		if (content.isEmpty()) {
			return Collections.emptyList();
		}

		return Arrays.stream(content.split(","))
			.map(String::trim)
			.map(Integer::parseInt)
			.toList();
	}
}