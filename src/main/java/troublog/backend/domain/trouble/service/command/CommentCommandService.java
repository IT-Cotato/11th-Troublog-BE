package troublog.backend.domain.trouble.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.Comment;
import troublog.backend.domain.trouble.repository.CommentRepository;

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

	public void delete(Comment comment) {
		log.info("[Post] 포스트 댓글 삭제: commentId={}, postId={}", comment.getId(), comment.getPost().getId());
		comment.getPost().removeComment(comment);
		comment.getUser().removeComment(comment);
		commentRepository.delete(comment);
	}

}
