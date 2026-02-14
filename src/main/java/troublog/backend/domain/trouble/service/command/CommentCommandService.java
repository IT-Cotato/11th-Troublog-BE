package troublog.backend.domain.trouble.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.Comment;
import troublog.backend.domain.trouble.repository.CommentRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentCommandService {
	private final CommentRepository commentRepository;

	public Comment save(Comment comment) {
		log.info("[Post] 포스트 댓글 저장: commentId={}, postId={}", comment.getId(), comment.getPost().getId());
		return commentRepository.save(comment);
	}

	public void softDelete(Comment comment) {
		Long postId = (comment.getPost() != null) ? comment.getPost().getId() : null;
		log.info("[Comment] 댓글 soft delete: commentId={}, postId={}", comment.getId(), postId);
		commentRepository.delete(comment);
	}

	public void hardDelete(final Comment comment) {
		Long commentId = comment.getId();
		Long postId = (comment.getPost() != null) ? comment.getPost().getId() : null;
		Long userId = (comment.getUser() != null) ? comment.getUser().getId() : null;
		if (commentRepository.existsByParentCommentId(comment.getId())) {
			throw new PostException(ErrorCode.COMMENT_HAS_CHILDREN);
		}
		log.info("[Comment] 댓글 하드 삭제: commentId={}, postId={}, userId={}", commentId, postId, userId);
		commentRepository.deleteHardById(comment.getId());
	}
}
