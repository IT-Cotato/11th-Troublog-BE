package troublog.backend.domain.trouble.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import troublog.backend.domain.trouble.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Page<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);

	boolean existsByParentCommentId(Long id);

	@Modifying
	@Query(
		value = "DELETE FROM comments WHERE comment_id = :commentId",
		nativeQuery = true
	)
	void deleteHardById(@Param("commentId") Long commentId);

	@Modifying
	@Query(
		value = """
			DELETE FROM comments
			WHERE deleted_at IS NOT NULL
				AND deleted_at <= :threshold
			""",
		nativeQuery = true
	)
	int deleteAllSoftDeletedBefore(@Param("threshold") LocalDateTime threshold);
}
