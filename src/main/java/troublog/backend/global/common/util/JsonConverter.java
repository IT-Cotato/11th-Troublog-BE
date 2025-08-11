package troublog.backend.global.common.util;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.experimental.UtilityClass;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.BusinessException;

@UtilityClass
public class JsonConverter {

	private static final ObjectMapper MAPPER = new ObjectMapper();
	public static final String SQUARE_BRACKETS = "[]";

	public String toJson(List<Integer> scores) {
		try {
			return MAPPER.writeValueAsString(scores == null ? Collections.emptyList() : scores);
		} catch (JsonProcessingException e) {
			throw new BusinessException(ErrorCode.JSON_PARSING_ERROR);
		}
	}

	public List<Integer> toList(String json) {
		if (json == null || json.trim().isEmpty() || SQUARE_BRACKETS.equals(json.trim())) {
			return Collections.emptyList();
		}
		try {
			return MAPPER.readValue(json, new TypeReference<List<Integer>>() {
			});
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
}