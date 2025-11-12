package troublog.backend.domain.trouble.service.facade.command;

import troublog.backend.domain.ai.summary.dto.response.TaskStartResDto;
import troublog.backend.domain.trouble.dto.request.PostReqDto;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.enums.SummaryType;

public interface PostCommandFacade {

	PostResDto createPost(Long userId, PostReqDto postReqDto);

	PostResDto updatePost(Long userId, Long postId, PostReqDto reqDto);

	void softDeletePost(Long userId, Long postId);

	void hardDeletePost(Long userId, Long postId);

	PostResDto restorePost(Long userId, Long postId);

	TaskStartResDto startSummary(Long userId, SummaryType summaryType, Long postId);
}
