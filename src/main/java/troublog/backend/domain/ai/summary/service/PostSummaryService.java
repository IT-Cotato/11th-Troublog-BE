package troublog.backend.domain.ai.summary.service;

import troublog.backend.domain.ai.common.service.AiTaskService;
import troublog.backend.domain.ai.summary.dto.SummarizedResDto;

public interface PostSummaryService extends AiTaskService<String, SummarizedResDto> {
}