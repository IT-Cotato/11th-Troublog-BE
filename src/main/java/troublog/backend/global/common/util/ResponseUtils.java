package troublog.backend.global.common.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.experimental.UtilityClass;
import troublog.backend.global.common.response.ApiResponse;
import troublog.backend.global.common.response.PageResponse;

@UtilityClass
public class ResponseUtils {
	public <T> ResponseEntity<ApiResponse<T>> ok(T data) {
		return ResponseEntity.ok(ApiResponse.ok(data));
	}

	public <T> ResponseEntity<ApiResponse<T>> created(T data) {
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(data));
	}

	public <T> ResponseEntity<ApiResponse<T>> noContent() {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.noContent());
	}

	public <T> ResponseEntity<PageResponse<T>> page(Page<T> page) {
		return ResponseEntity.ok(PageResponse.of(page));
	}
}