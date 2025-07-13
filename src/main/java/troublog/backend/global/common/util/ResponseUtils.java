package troublog.backend.global.common.util;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.experimental.UtilityClass;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.response.PageResponse;

@UtilityClass
public class ResponseUtils {
	public <T> ResponseEntity<BaseResponse<T>> ok(T data) {
		return ResponseEntity.ok(BaseResponse.ok(data));
	}

	public <T> ResponseEntity<BaseResponse<T>> created(T data) {
		return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.created(data));
	}

	public <T> ResponseEntity<BaseResponse<T>> noContent() {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(BaseResponse.noContent());
	}

	public <T> ResponseEntity<PageResponse<T>> page(Page<T> page) {
		return ResponseEntity.ok(PageResponse.of(page));
	}
}