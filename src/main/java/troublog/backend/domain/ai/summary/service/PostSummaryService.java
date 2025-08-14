package troublog.backend.domain.ai.summary.service;

import troublog.backend.domain.ai.common.service.AiTaskService;
import troublog.backend.domain.ai.summary.dto.response.SummarizedResDto;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.trouble.enums.ContentSummaryType;

public interface PostSummaryService extends AiTaskService<SummaryTask, ContentSummaryType, SummarizedResDto> {
}