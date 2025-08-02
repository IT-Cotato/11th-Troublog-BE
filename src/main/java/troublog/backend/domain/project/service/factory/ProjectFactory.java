package troublog.backend.domain.project.service.factory;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectFactory {

	public static void validateAuthorized(Long requestUserID, Project project) {
		Long registeredUserID = project.getUser().getId();
		if (!registeredUserID.equals(requestUserID)) {
			throw new PostException(ErrorCode.POST_ACCESS_DENIED);
		}
	}
}
