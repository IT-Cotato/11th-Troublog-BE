package troublog.backend.domain.trouble.service.command;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
		log.info("[Comment] 댓글 soft delete: commentId={}, postId={}", comment.getId(), comment.getPost().getId());
		commentRepository.delete(comment);
	}

	public void softDeleteAll(List<Comment> commentList) {
		if (CollectionUtils.isEmpty(commentList)) {
			log.info("[Comment] 삭제할 댓글 없음");
			return;
		}

		log.info("[Comment] 댓글 soft delete all: commentList={}", commentList);
		commentList.forEach(Comment::markDeleted);
	}

	public void hardDelete(final Comment comment) {
		Long commentId = comment.getId();
		Long postId = (comment.getPost() != null) ? comment.getPost().getId() : null;
		Long userId = (comment.getUser() != null) ? comment.getUser().getId() : null;
		if (commentRepository.existsByParentCommentId(comment.getId())) {
			throw new PostException(ErrorCode.COMMENT_HAS_CHILDREN);
		}
		log.info("[Comment] 댓글 하드 삭제: commentId={}, postId={}, userId={}", commentId, postId, userId);
		if (comment.getPost() != null) {
			comment.getPost().removeComment(comment);
		}
		if (comment.getUser() != null) {
			comment.getUser().removeComment(comment);
		}
		commentRepository.deleteHardById(comment.getId());
	}

	public void hardDeleteAll(final List<Long> hardDeleteCommentIdList) {
		if (!CollectionUtils.isEmpty(hardDeleteCommentIdList)) {
			log.info("[Comment] 댓글 하드 삭제: commentList={}", hardDeleteCommentIdList);
			commentRepository.deleteHardAll(hardDeleteCommentIdList);
		}
	}
}
