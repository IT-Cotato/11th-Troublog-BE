package troublog.backend.domain.trouble.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;
import troublog.backend.domain.trouble.entity.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {

	@Query("select c from Content c left join Post p on p.id = c.post.id where c.post.id = :postId")
	List<Content> findAllByPostId(@Param("postId") Long postId);
}