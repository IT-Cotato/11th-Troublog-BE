package troublog.backend.global.common.response;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;

@Builder
public record ApiResponse<T> (
    @JsonIgnore
    HttpStatus httpStatus,
    boolean success,
    @Nullable T data,
    @Nullable ErrorResponse error,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	LocalDateTime timestamp
) {

    /**
     * HTTP 200 OK
     */
    public static <T> ApiResponse<T> ok(@Nullable final T data) {
        return ApiResponse.<T>builder()
            .httpStatus(HttpStatus.OK)
            .success(true)
            .data(data)
            .error(null)
			.timestamp(LocalDateTime.now())
            .build();
    }

    /**
     * HTTP 201 Created
     */
    public static <T> ApiResponse<T> created(@Nullable final T data) {
        return ApiResponse.<T>builder()
            .httpStatus(HttpStatus.CREATED)
            .success(true)
            .data(data)
            .error(null)
			.timestamp(LocalDateTime.now())
            .build();
    }

    /**
     * HTTP 204 No Content
     */
    public static <T> ApiResponse<T> noContent() {
        return ApiResponse.<T>builder()
            .httpStatus(HttpStatus.NO_CONTENT)
            .success(true)
            .data(null)
            .error(null)
			.timestamp(LocalDateTime.now())
            .build();
    }

    /**
     * Error Response
     */
    public static <T> ApiResponse<T> fail(ErrorResponse error) {
        return ApiResponse.<T>builder()
            .httpStatus(error.status())
            .success(false)
            .data(null)
            .error(error)
			.timestamp(LocalDateTime.now())
            .build();
    }
}