package troublog.backend.domain.project.service.facade;

import static troublog.backend.domain.project.service.factory.ProjectFactory.*;
import static troublog.backend.global.common.error.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.project.dto.response.ProjectDetailResDto;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.project.service.query.ProjectQueryService;
import troublog.backend.domain.trouble.dto.response.TroubleListResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.ContentSummaryType;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.enums.SortType;
import troublog.backend.domain.trouble.enums.VisibilityType;
import troublog.backend.global.common.error.exception.PostException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectQueryFacade {

	private final ProjectQueryService projectQueryService;

	public ProjectDetailResDto getDetailsProject(Long userId, long projectId) {
		Project project = projectQueryService.findById(projectId);
		validateProjectAuthorized(userId, project);
		return projectQueryService.getDetails(project);
	}

	public List<TroubleListResDto> getProjectTroubles(Long userId, Long projectId, PostStatus status, SortType sort,
		VisibilityType visibility, ContentSummaryType summaryType) {
		//해당 유저의 프로젝트 아이디가 있는지 검증
		Project project = projectQueryService.findById(projectId);
		validateProjectAuthorized(userId, project);

		//프로젝트 아이디안에 포스트가 있는지 검증
		List<Post> posts = project.getPosts();
		if (posts == null || posts.isEmpty()) {
			return List.of();
		}

		if (status == status.COMPLETED) {
			//최신순, 중요도순 (sort)
			//전체, 공개, 바공개 (visibility)

		} else if (status == status.SUMMARIZED) {
			//최신순, 중요도순 (sort)
			//전체, 자기소개서.. (summaryType)

		} else
			throw new PostException(INVALID_VALUE);

	}
}
