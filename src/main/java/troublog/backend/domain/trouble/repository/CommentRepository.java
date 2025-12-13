package troublog.backend.domain.trouble.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import troublog.backend.domain.trouble.entity.Comment;
import troublog.backend.domain.user.entity.User;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Page<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);

	boolean existsByParentCommentId(Long id);

	@Query("""
		select distinct c
		from Comment c
		left join fetch c.childComments cc
		where c.user = :user
		""")
	List<Comment> findAllByUserWithChildren(@Param("user") User user);

	@Modifying
	@Query(
		value = "DELETE FROM comments WHERE comment_id = :commentId",
		nativeQuery = true
	)
	void deleteHardById(@Param("commentId") Long commentId);

	@Modifying
	@Query(
		value = "DELETE FROM comments WHERE comment_id IN :ids",
		nativeQuery = true
	)
	void deleteHardAll(@Param("ids") List<Long> hardDeleteCommentIdList);

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
