package troublog.backend.domain.ai.summary.dto.response;

import java.util.List;

import troublog.backend.domain.trouble.dto.request.common.SummaryContentDto;

public record SummarizedResDto(List<SummaryContentDto> contentDtoList) {
}