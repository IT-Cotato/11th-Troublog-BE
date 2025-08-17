package troublog.backend.domain.trouble.service.facade.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.converter.PostSummaryConverter;
import troublog.backend.domain.trouble.converter.SummaryContentConverter;
import troublog.backend.domain.trouble.dto.response.PostSummaryResDto;
import troublog.backend.domain.trouble.dto.response.common.SummaryContentInfoDto;
import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.entity.SummaryContent;
import troublog.backend.domain.trouble.service.query.PostSummaryQueryService;
import troublog.backend.domain.trouble.validator.PostValidator;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSummaryQueryFacade {

	private final PostSummaryQueryService postSummaryQueryService;

	public PostSummaryResDto findPostSummaryById(Long userId, Long summaryId) {
		PostSummary postSummary = postSummaryQueryService.findById(summaryId);
		PostValidator.validateSummaryBelongsToUser(userId, postSummary);
		return PostSummaryConverter.toResponse(postSummary);
	}

	public static List<SummaryContentInfoDto> findSummaryContents(PostSummary postSummary) {
		if (CollectionUtils.isEmpty(postSummary.getSummaryContents())) {
			return List.of();
		}
		List<SummaryContent> summaryContents = postSummary.getSummaryContents();
		return SummaryContentConverter.toResponseList(summaryContents);
	}
}
