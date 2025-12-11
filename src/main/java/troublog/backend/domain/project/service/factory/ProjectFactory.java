package troublog.backend.domain.project.service.factory;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.ProjectException;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectFactory {

	public static void validateProjectAuthorized(Long requestUserID, Project project) {
		Long registeredUserID = project.getUser().getId();
		if (!registeredUserID.equals(requestUserID)) {
			throw new ProjectException(ErrorCode.PROJECT_ACCESS_DENIED);
		}
	}
}
