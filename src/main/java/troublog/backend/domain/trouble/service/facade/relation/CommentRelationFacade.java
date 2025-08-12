package troublog.backend.domain.trouble.service.facade.relation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.entity.Comment;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.user.entity.User;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRelationFacade {

	public void establishRelations(Comment createdComment, User user, Post post) {
		setUserRelations(createdComment, user);
		setPostRelations(createdComment, post);
	}

	public void establishChildRelations(Comment parentComment, Comment childComment) {
		parentComment.addChildComment(childComment);
	}

	private void setPostRelations(Comment comment, Post post) {
		post.addComment(comment);
	}

	private void setUserRelations(Comment comment, User user) {
		user.addComment(comment);
	}

}
