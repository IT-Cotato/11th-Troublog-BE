package troublog.backend.domain.ai.summary.dto;

import lombok.Builder;

@Builder
public record ExtractedContentDto(String body, int sequence) {
}