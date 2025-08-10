package troublog.backend.domain.project.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}