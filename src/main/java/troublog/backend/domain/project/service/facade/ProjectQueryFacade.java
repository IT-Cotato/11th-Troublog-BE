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
import troublog.backend.domain.project.service.query.ProjectQueryService;
import troublog.backend.domain.trouble.dto.response.TroubleListResDto;
import troublog.backend.domain.trouble.enums.PostViewFilter;
import troublog.backend.domain.trouble.enums.SortType;
import troublog.backend.domain.trouble.enums.SummaryType;
import troublog.backend.domain.trouble.enums.VisibilityType;
import troublog.backend.domain.trouble.service.query.PostQueryService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectQueryFacade {

	private final ProjectQueryService projectQueryService;
	private final PostQueryService postQueryService;

	public ProjectDetailResDto getDetailsProject(final Long userId, final long projectId) {
		Project project = projectQueryService.findById(projectId);
		validateProjectAuthorized(userId, project);
		return projectQueryService.getDetails(project);
	}

	public Pageable getPageable(final int page, final int size) {
		return PageRequest.of(Math.max(0, page - 1), size);
	}

	public Page<ProjectDetailResDto> getAllProjects(final Long userId, final Pageable pageable) {
		return projectQueryService.getAllProjects(userId, pageable);
	}

	public List<TroubleListResDto> getProjectTroubles(
		final Long userId,
		final Long projectId,
		final PostViewFilter statusType,
		final SortType sort,
		final VisibilityType visibility
	) {
		Project project = projectQueryService.findById(projectId);
		validateProjectAuthorized(userId, project);
		return postQueryService.getProjectTroublesByStatus(projectId, sort, visibility, statusType);
	}

	public List<TroubleListResDto> getProjectTroubleSummaries(
		final Long userId,
		final Long projectId,
		final SortType sort,
		final SummaryType summaryType
	) {
		Project project = projectQueryService.findById(projectId);
		validateProjectAuthorized(userId, project);
		return postQueryService.getSummarizedTroubles(projectId, sort, summaryType);
	}
}

