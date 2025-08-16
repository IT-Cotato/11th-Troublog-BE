package troublog.backend.domain.trouble.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import troublog.backend.domain.trouble.entity.PostTag;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

	@Query("SELECT pt FROM PostTag pt JOIN FETCH pt.tag WHERE pt.post.id = :postId")
	List<PostTag> findAllByPostIdWithTag(@Param("postId") Long postId);

	@Query("SELECT DISTINCT t.name FROM PostTag pt JOIN pt.tag t WHERE pt.post.id = :postId ORDER BY t.name")
	List<String> findTagNamesByPostId(@Param("postId") Long postId);
}