package troublog.backend.domain.trouble.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import troublog.backend.domain.trouble.entity.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {
	List<Content> findAllByPostId(@Param("postId") Long postId);

	@Modifying
	@Query(
		value = """
			DELETE FROM contents
			WHERE deleted_at IS NOT NULL
				AND deleted_at <= :threshold
			""",
		nativeQuery = true
	)
	int deleteAllSoftDeletedBefore(@Param("threshold") LocalDateTime threshold);
}
