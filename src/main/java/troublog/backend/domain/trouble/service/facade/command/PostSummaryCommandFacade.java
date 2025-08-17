package troublog.backend.domain.trouble.service.facade.command;

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
import troublog.backend.domain.trouble.service.facade.relation.PostSummaryRelationFacade;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSummaryCommandFacade {

	private final PostSummaryRelationFacade postSummaryRelationFacade;
	private final PostSummaryCommandService postSummaryCommandService;

	public PostSummary createPostSummary(SummaryTask summaryTask, SummarizedResDto result) {
		PostSummary newPostSummary = PostSummaryConverter.toEntity(summaryTask);
		List<SummaryContent> summaryContents = SummaryContentConverter.toEntityList(result);
		postSummaryRelationFacade.setRelation(newPostSummary, summaryContents);
		return postSummaryCommandService.save(newPostSummary);
	}
}
