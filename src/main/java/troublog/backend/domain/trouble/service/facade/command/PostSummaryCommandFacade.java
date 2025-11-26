package troublog.backend.domain.trouble.service.facade.command;

import troublog.backend.domain.ai.summary.dto.response.SummarizedResDto;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.trouble.entity.PostSummary;

public interface PostSummaryCommandFacade {

	PostSummary createPostSummary(SummaryTask summaryTask, SummarizedResDto result);

	void hardDeletePostSummary(Long userId, Long summaryId);
}
