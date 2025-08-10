package troublog.backend.domain.trouble.service.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.dto.request.CommentReqDto;
import troublog.backend.domain.trouble.entity.Comment;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.service.query.PostQueryService;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.service.query.UserQueryService;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRelationFacade {

	private final PostQueryService postQueryService;
	private final UserQueryService userQueryService;

	public void establishRelations(Comment createdComment, Long userId, CommentReqDto commentReqDto) {
		setUserRelations(createdComment, userId);
		setPostRelations(createdComment, commentReqDto.postId());
	}

	public void establishChildRelations(Comment parentComment, Comment childComment) {
		parentComment.addChildComment(childComment);
	}

	public void setPostRelations(Comment comment, Long postId) {
		Post post = postQueryService.findById(postId);
		post.addComment(comment);
	}

	public void setUserRelations(Comment comment, Long userId) {
		User user = userQueryService.findUserById(userId);
		user.addComment(comment);
	}

}
