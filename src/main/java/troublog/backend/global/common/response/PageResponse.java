package troublog.backend.global.common.response;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Builder;

@Builder
public record PageResponse<T>(
	List<T> content,
	Boolean hasNext,
	int totalPages,
	long totalElements,
	int page,
	int size,
	Boolean isFirst,
	Boolean isLast
) {
	public static <T> PageResponse<T> of(Page<T> page) {
		return PageResponse.<T>builder()
			.content(page.getContent())
			.hasNext(page.hasNext())
			.totalPages(page.getTotalPages())
			.totalElements(page.getTotalElements())
			.page(page.getNumber())
			.size(page.getSize())
			.isFirst(page.isFirst())
			.isLast(page.isLast())
			.build();
	}
}