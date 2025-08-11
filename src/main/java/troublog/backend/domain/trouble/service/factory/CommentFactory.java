package troublog.backend.domain.trouble.service.factory;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.entity.Comment;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentFactory {

	public static void validateAuthorized(Long userId, Comment comment) {
		Long originUserId = comment.getUser().getId();
		if (!userId.equals(originUserId)) {
			throw new PostException(ErrorCode.COMMENT_ACCESS_DENIED);
		}
	}

	public static void validateParent(Comment parentComment) {
		if (parentComment.getParentComment() != null) {
			throw new PostException(ErrorCode.COMMENT_NOT_PARENT);
		}
	}
}
