package troublog.backend.domain.trouble.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.trouble.entity.Comment;
import troublog.backend.domain.user.entity.User;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Page<Comment> findByPostIdAndIsDeletedFalseOrderByCreatedAtDesc(Long postId, Pageable pageable);

	boolean existsByParentCommentId(Long id);

	List<Comment> findAllByUserAndIsDeletedFalse(User user);
}
