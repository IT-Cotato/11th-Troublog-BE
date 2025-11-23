package troublog.backend.domain.trouble.service.facade.command;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.ai.summary.dto.response.SummarizedResDto;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.trouble.converter.PostSummaryConverter;
import troublog.backend.domain.trouble.converter.SummaryContentConverter;
import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.entity.SummaryContent;
import troublog.backend.domain.trouble.service.command.PostSummaryCommandService;
import troublog.backend.domain.trouble.service.facade.relation.PostSummaryRelationFacade;
import troublog.backend.domain.trouble.service.query.PostSummaryQueryService;
import troublog.backend.domain.trouble.validator.PostValidator;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSummaryCommandFacadeImpl implements PostSummaryCommandFacade {

	private final PostSummaryRelationFacade postSummaryRelationFacade;
	private final PostSummaryCommandService commandService;
	private final PostSummaryQueryService queryService;

	public PostSummary createPostSummary(final SummaryTask summaryTask, final SummarizedResDto result) {
		PostSummary newPostSummary = PostSummaryConverter.toEntity(summaryTask);
		List<SummaryContent> summaryContents = SummaryContentConverter.toEntityList(result);
		postSummaryRelationFacade.setRelation(newPostSummary, summaryContents);
		return commandService.save(newPostSummary);
	}

	@Override
	public void hardDeletePostSummary(final Long userId, final Long summaryId) {
		PostSummary postSummary = queryService.findById(summaryId);
		PostValidator.validateSummaryBelongsToUser(userId, postSummary);
		commandService.delete(postSummary);
	}
}