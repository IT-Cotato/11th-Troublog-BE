package troublog.backend.domain.trouble.validator;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@UtilityClass
public class PostValidator {

	public void validateVisibility(Post post) {
		if (Boolean.FALSE.equals(post.getIsVisible())) {
			throw new PostException(ErrorCode.POST_NOT_VISIBLE);
		}
	}
}
