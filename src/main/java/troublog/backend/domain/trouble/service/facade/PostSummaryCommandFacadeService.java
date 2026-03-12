package troublog.backend.domain.trouble.service.facade;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.ai.summary.dto.response.SummarizedResDto;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.trouble.converter.PostSummaryConverter;
import troublog.backend.domain.trouble.converter.SummaryContentConverter;
import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.entity.SummaryContent;
import troublog.backend.domain.trouble.service.command.PostSummaryCommandService;
import troublog.backend.domain.trouble.service.query.PostSummaryQueryService;
import troublog.backend.domain.trouble.validator.PostValidator;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSummaryCommandFacadeService {

	private final PostSummaryRelationFacadeService postSummaryRelationFacadeService;
	private final PostSummaryCommandService commandService;
	private final PostSummaryQueryService queryService;

	public PostSummary createPostSummary(final SummaryTask summaryTask, final SummarizedResDto result) {
		PostSummary newPostSummary = PostSummaryConverter.toEntity(summaryTask);
		List<SummaryContent> summaryContents = SummaryContentConverter.toEntityList(result);
		postSummaryRelationFacadeService.setRelation(newPostSummary, summaryContents);
		return commandService.save(newPostSummary);
	}

	public void hardDeletePostSummary(final Long userId, final Long summaryId) {
		PostSummary postSummary = queryService.findById(summaryId);
		PostValidator.validateSummaryBelongsToUser(userId, postSummary);
		commandService.delete(postSummary);
	}
}
