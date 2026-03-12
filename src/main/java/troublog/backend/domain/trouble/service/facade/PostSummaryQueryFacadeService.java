package troublog.backend.domain.trouble.service.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.converter.PostSummaryConverter;
import troublog.backend.domain.trouble.dto.response.PostSummaryResDto;
import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.service.query.PostQueryService;
import troublog.backend.domain.trouble.service.query.PostSummaryQueryService;
import troublog.backend.domain.trouble.validator.PostValidator;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSummaryQueryFacadeService {

	private final PostQueryService postQueryService;
	private final PostSummaryQueryService postSummaryQueryService;

	public PostSummaryResDto findPostSummaryById(final Long userId, final Long summaryId) {
		PostSummary postSummary = postSummaryQueryService.findById(summaryId);
		PostValidator.validateSummaryBelongsToUser(userId, postSummary);
		return toPostSummaryResponse(postSummary);
	}

	private PostSummaryResDto toPostSummaryResponse(final PostSummary postSummary) {
		return PostSummaryConverter.toResponse(
			postSummary,
			postQueryService.findErrorTag(postSummary.getPost()),
			postQueryService.findTechStackTags(postSummary.getPost())
		);
	}
}
