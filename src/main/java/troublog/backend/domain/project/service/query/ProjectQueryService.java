package troublog.backend.domain.project.service.query;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.project.converter.ProjectConverter;
import troublog.backend.domain.project.dto.response.ProjectDetailResDto;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.project.repository.ProjectRepository;
import troublog.backend.domain.trouble.enums.TagType;
import troublog.backend.domain.user.entity.User;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.ProjectException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectQueryService {

	private final ProjectRepository projectRepository;

	public Project findByIdAndIsDeletedFalse(long id) {
		log.info("[Project] 프로젝트 조회: projectId={}", id);
		return projectRepository.findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new ProjectException(ErrorCode.PROJECT_NOT_FOUND));
	}

	public ProjectDetailResDto getDetails(Project project) {
		log.info("[Project] 상세조회: projectId={}, projectName={}", project.getId(), project.getName());

		List<String> topTags = projectRepository.findTop2TagsByProjectId(
			project.getId(), TagType.TECH_STACK, PageRequest.of(0, 2)
		);
		log.info("[Project] 태그 조회 결과: projectId={}, topTags={}", project.getId(), topTags);
		return ProjectConverter.toResponseDetail(project, topTags);
	}

	public Page<ProjectDetailResDto> getAllProjects(Long userId, Pageable pageable) {
		Page<Project> page = projectRepository.findAllByUserIdAndIsDeletedFalse(userId, pageable);
		log.info("[Project] 전체 프로젝트 조회: userId={}, projectCount={}", userId, page.getSize());
		return page.map(this::getDetails);
	}

	public List<Project> getAllProjectsByUser(User user) {
		List<Project> projectList = projectRepository.findAllByUserAndIsDeletedFalse(user);
		log.info("[Project] 특정 유저의 삭제되지 않은 프로젝트 조회: userId={}, projectCount={}", user.getId(), projectList.size());
		return projectList;
	}
}