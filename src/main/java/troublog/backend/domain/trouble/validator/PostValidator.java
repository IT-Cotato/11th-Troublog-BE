package troublog.backend.domain.trouble.validator;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.entity.Comment;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@UtilityClass
public class PostValidator {

	public void validateVisibility(Post post) {
		if (post != null && !Boolean.TRUE.equals(post.getIsVisible())) {
			throw new PostException(ErrorCode.POST_ACCESS_DENIED);
		}
	}

	public static void validateCommentBelongsToPost(Comment comment, Long postId) {
		if (postId == null) {
			return;
		}
		if (comment == null || comment.getPost() == null || !comment.getPost().getId().equals(postId)) {
			throw new PostException(ErrorCode.COMMENT_NOT_PARENT);
		}
	}

	public boolean isValidSummaryContent(Post post) {
		if (post == null) {
			return false;
		}
		return Boolean.TRUE.equals(post.getIsSummaryCreated())
			&& post.getContents() != null
			&& !post.getContents().isEmpty()
			&& post.getContents().getFirst() != null
			&& post.getContents().getFirst().getSummaryType() != null;
	}
}
