package troublog.backend.domain.trouble.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.trouble.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Page<Comment> findByPostIdAndIsDeletedFalseOrderByCreatedAtDesc(Long postId, Pageable pageable);
}
