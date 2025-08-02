package troublog.backend.domain.project.service.facade;

import static troublog.backend.domain.project.service.factory.ProjectFactory.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.project.converter.ProjectConverter;
import troublog.backend.domain.project.dto.request.ProjectReqDto;
import troublog.backend.domain.project.dto.response.ProjectResDto;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.project.service.command.ProjectCommandService;
import troublog.backend.domain.project.service.query.ProjectQueryService;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.service.query.UserQueryService;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectCommandFacade {

	private final UserQueryService userQueryService;
	private final ProjectCommandService projectCommandService;
	private final ProjectQueryService projectQueryService;

	public ProjectResDto createProject(Long userId, ProjectReqDto reqDto) {
		Project project = ProjectConverter.toEntity(reqDto);
		User user = userQueryService.findUserById(userId);
		user.addProject(project);
		Project savedProject = projectCommandService.save(project);
		return ProjectConverter.toResponse(savedProject);
	}

	public ProjectResDto updateProject(Long userId, ProjectReqDto reqDto, Long projectId) {
		Project project = projectQueryService.findById(projectId);
		validateAuthorized(userId, project);
		project.update(reqDto.name(), reqDto.description(), reqDto.thumbnailImageUrl());
		return ProjectConverter.toResponse(project);
	}

	public void softDeleteProject(Long userId, long projectId) {
		Project project = projectQueryService.findById(projectId);
		validateAuthorized(userId, project);
		project.softDelete();
	}
}
