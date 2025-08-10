package troublog.backend.domain.ai.summary.dto.common;

import lombok.Builder;

@Builder
public record ExtractedContentDto(String body, int sequence) {
}