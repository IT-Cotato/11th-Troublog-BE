package troublog.backend.domain.project.service.facade;

import static troublog.backend.domain.project.service.factory.ProjectFactory.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.project.dto.response.ProjectDetailResDto;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.project.enums.ProjectPostStatus;
import troublog.backend.domain.project.service.query.ProjectQueryService;
import troublog.backend.domain.trouble.dto.response.TroubleListResDto;
import troublog.backend.domain.trouble.enums.ContentSummaryType;
import troublog.backend.domain.trouble.enums.SortType;
import troublog.backend.domain.trouble.enums.VisibilityType;
import troublog.backend.domain.trouble.service.query.PostQueryService;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.ProjectException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectQueryFacade {

	private final ProjectQueryService projectQueryService;
	private final PostQueryService postQueryService;

	public ProjectDetailResDto getDetailsProject(Long userId, long projectId) {
		Project project = projectQueryService.findById(projectId);
		validateProjectAuthorized(userId, project);
		return projectQueryService.getDetails(project);
	}

	public Pageable getPageable(int page, int size) {
		return PageRequest.of(Math.max(0, page - 1), size);
	}

	public Page<ProjectDetailResDto> getAllProjects(Long userId, Pageable pageable) {
		return projectQueryService.getAllProjects(userId, pageable);
	}

	public List<TroubleListResDto> getProjectTroubles(Long userId, Long projectId, ProjectPostStatus status,
		SortType sort,
		VisibilityType visibility, ContentSummaryType summaryType) {
		Project project = projectQueryService.findById(projectId);
		validateProjectAuthorized(userId, project);

		if (status == ProjectPostStatus.COMPLETED) {
			return postQueryService.getCompletedTroubles(projectId, sort, visibility);
		} else if (status == ProjectPostStatus.SUMMARIZED) {
			return postQueryService.getSummarizedTroubles(projectId, sort, summaryType);
		} else
			throw new ProjectException(ErrorCode.INVALID_VALUE);
	}
}

