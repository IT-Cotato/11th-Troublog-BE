package troublog.backend.global.common.util;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.BusinessException;

@Slf4j
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
		String trimmed = (json == null) ? null : json.trim();
		if (StringUtils.isEmpty(trimmed)
			|| SQUARE_BRACKETS.equals(trimmed)
			|| "null".equalsIgnoreCase(trimmed)) {
			return Collections.emptyList();
		}
		try {
			return MAPPER.readValue(trimmed, new TypeReference<>() {
			});
		} catch (Exception e) {
			log.error("JSON 파싱 실패 JSON -> List<Integer>: {}", trimmed, e);
			return Collections.emptyList(); // 필요 시 예외 전환으로 정책 일치
		}
	}
}