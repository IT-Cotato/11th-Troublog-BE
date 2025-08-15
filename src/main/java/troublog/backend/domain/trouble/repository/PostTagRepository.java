package troublog.backend.domain.trouble.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import troublog.backend.domain.trouble.entity.PostTag;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

	@Query("SELECT pt FROM PostTag pt JOIN FETCH pt.tag WHERE pt.post.id = :postId")
	List<PostTag> findAllByPostIdWithTag(@Param("postId") Long postId);

	@Query(value = """
		  SELECT DISTINCT t.name
		    FROM post_tags pt
		    JOIN tags t ON pt.tag_id = t.tag_id
		   WHERE pt.post_id = :postId
		   ORDER BY t.tag_id
		""", nativeQuery = true)
	List<String> findTagNamesByPostId(@Param("postId") Long postId);
}