package troublog.backend.domain.project.service.command;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.project.repository.ProjectRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectCommandService {

	private final ProjectRepository projectRepository;

	public Project save(Project project) {
		Project saved = projectRepository.save(project);
		log.info("[Project] 프로젝트 생성: projectId={}, name={}", saved.getId(), saved.getName());
		return saved;
	}

	public void delete(Project project) {
		Long projectId = project.getId();
		log.info("[Project] 프로젝트 하드 삭제: projectId={}", projectId);
		projectRepository.delete(project);
	}

	public void softDelete(Project project) {
		log.info("[Project] 프로젝트 soft delete: projectId={}", project.getId());
		project.softDelete();
	}

	public void softDeleteAll(List<Project> projectList) {
		if (CollectionUtils.isEmpty(projectList)) {
			log.info("[Project] 삭제할 프로젝트 없음");
			return;
		}

		log.info("[Project] 프로젝트 리스트 soft delete:  projectList={}", projectList);
		projectList.forEach(Project::softDelete);
	}
}