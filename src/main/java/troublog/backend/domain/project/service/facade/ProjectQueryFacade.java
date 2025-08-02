package troublog.backend.domain.project.service.facade;

import static troublog.backend.domain.project.service.factory.ProjectFactory.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.project.dto.response.ProjectDetailResDto;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.project.service.query.ProjectQueryService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectQueryFacade {

	private final ProjectQueryService projectQueryService;

	public ProjectDetailResDto getDetailsProject(Long userId, long projectId) {
		Project project = projectQueryService.findById(projectId);
		validateAuthorized(userId, project);
		return projectQueryService.getDetails(project);
	}

}
