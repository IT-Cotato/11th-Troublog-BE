package troublog.backend.domain.trouble.dto.response;

import lombok.Builder;

@Builder
public record CombineResDto(
	PostResDto postResDto,
	PostSummaryResDto postSummaryResDto
) {
}
