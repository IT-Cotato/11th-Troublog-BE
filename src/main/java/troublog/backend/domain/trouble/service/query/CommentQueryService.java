package troublog.backend.domain.trouble.service.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentQueryService {
	private final CommentRepository commentRepository;

	public Comment findComment(Long id) {
		log.info("[Post] 트러블슈팅 댓글 조회: postId={}", id);
		return commentRepository.findById(id)
			.orElseThrow(() -> new PostException(ErrorCode.COMMENT_NOT_FOUND));
	}

	public Page<Comment> findAllComment(Long postId, Pageable pageable) {
		Page<Comment> comments = commentRepository.findByPostIdAndIsDeletedFalseOrderByCreatedAtDesc(postId, pageable);
		log.info("[Post] 전체 댓글 조회: postId={} size={}", postId, comments.getTotalElements());
		return comments;
	}
}
