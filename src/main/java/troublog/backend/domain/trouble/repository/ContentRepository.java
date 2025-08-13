package troublog.backend.domain.trouble.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import troublog.backend.domain.trouble.entity.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {
	List<Content> findAllByPostId(@Param("postId") Long postId);

	@Query("SELECT c FROM Content c WHERE c.post.id = :postId AND c.authorType = 'USER_WRITTEN'")
	List<Content> findContentsWithoutSummaryByPostId(@Param("postId") Long postId);
}