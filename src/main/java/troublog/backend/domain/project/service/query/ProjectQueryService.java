package troublog.backend.domain.project.service.query;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.project.repository.ProjectRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.ProjectException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectQueryService {

	private final ProjectRepository projectRepository;

	public Project findById(long id) {
		log.info("[Project] 프로젝트 조회: projectId={}", id);
		return projectRepository.findById(id)
			.orElseThrow(() -> new ProjectException(ErrorCode.PROJECT_NOT_FOUND));
	}
}