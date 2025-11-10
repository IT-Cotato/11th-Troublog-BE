package troublog.backend.domain.trouble.service.facade.relation;

import java.util.List;

import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.trouble.dto.request.PostReqDto;
import troublog.backend.domain.trouble.dto.request.common.ContentDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostSummary;

public interface PostRelationFacade {
	void establishRequireRelations(Post createdPost, Long userId, PostReqDto postReqDto);

	void establishSecondaryRelations(Post post, PostReqDto postReqDto);

	void setProjectRelations(Post post, Long projectId);

	void setUserRelations(Post post, Long userId);

	void setErrorTagRelations(Post post, String tagName);

	void setContentRelations(Post post, List<ContentDto> contentDtoList);

	void setPostSummaryRelation(Post post, PostSummary postSummary, SummaryTask summaryTask);
}
