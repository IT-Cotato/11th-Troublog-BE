package troublog.backend.domain.ai.summary.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.ai.summary.dto.response.SummarizedResDto;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.ai.summary.enums.SummaryStatus;
import troublog.backend.domain.ai.summary.service.facade.SummaryTaskFacade;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.service.facade.command.PostSummaryCommandFacade;
import troublog.backend.domain.trouble.service.facade.query.PostQueryFacade;
import troublog.backend.domain.trouble.service.facade.relation.PostRelationFacade;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSummaryCompletionService {

	private final SummaryTaskFacade summaryTaskFacade;
	private final PostQueryFacade postQueryFacade;
	private final PostRelationFacade postRelationFacade;
	private final PostSummaryCommandFacade postSummaryCommandFacade;

	@Transactional
	public SummarizedResDto completeTask(SummaryTask summaryTask, SummarizedResDto result) {
		Post foundPost = postQueryFacade.findPostEntityById(summaryTask.getPostId(), summaryTask.getUserId());
		PostSummary postSummary = postSummaryCommandFacade.createPostSummary(summaryTask, result);
		postRelationFacade.setPostSummaryRelation(foundPost, postSummary, summaryTask);
		log.info("AI 분석 작업 완료: taskId={}, postId={}", summaryTask.getId(), summaryTask.getPostId());
		summaryTaskFacade.updateTask(summaryTask, SummaryStatus.COMPLETED);

		return result;
	}
}