package troublog.backend.domain.trouble.validator;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.entity.Comment;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@UtilityClass
public class PostValidator {

	public void validateVisibility(Post post) {
		if (post == null || Boolean.FALSE.equals(post.getIsVisible())) {
			throw new PostException(ErrorCode.POST_NOT_VISIBLE);
		}
	}

	public static void validateCommentBelongsToPost(Comment comment, Long postId) {
		if (postId != null && !comment.getPost().getId().equals(postId)) {
			throw new PostException(ErrorCode.COMMENT_NOT_PARENT);
		}
	}

	public boolean isValidSummaryContent(Post post) {
		return Boolean.TRUE.equals(post.getIsSummaryCreated())
			&& post.getContents() != null
			&& !post.getContents().isEmpty()
			&& post.getContents().getFirst() != null
			&& post.getContents().getFirst().getSummaryType() != null;
	}
}
