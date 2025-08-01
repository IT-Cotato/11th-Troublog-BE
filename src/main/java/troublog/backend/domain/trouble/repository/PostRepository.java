package troublog.backend.domain.trouble.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import troublog.backend.domain.trouble.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	Optional<Post> findByIdAndIsDeletedFalse(Long id);

	List<Post> findByIsDeletedFalse();

	List<Post> findByIsDeletedTrue();

	@Query(value = """
		SELECT DISTINCT p.*
		FROM posts p
		WHERE p.user_id = :userId
		  AND p.is_deleted = false
		  AND (
		    p.title LIKE CONCAT('%', :keyword, '%')
		    OR
		    EXISTS (
		      SELECT 1 FROM contents c
		      WHERE c.post_id = p.post_id
		        AND c.body LIKE CONCAT('%', :keyword, '%')
		    )
		    OR
		    EXISTS (
		      SELECT 1 FROM post_tags pt
		      INNER JOIN tags t ON pt.tag_id = t.tag_id
		      WHERE pt.post_id = p.post_id
		        AND t.name LIKE CONCAT('%', :keyword, '%')
		    )
		  )
		ORDER BY p.created_at DESC
		LIMIT :limit OFFSET :offset
		""", nativeQuery = true)
	List<Post> searchPostsByKeyword(
		@Param("userId") Long userId,
		@Param("keyword") String keyword,
		@Param("offset") int offset,
		@Param("limit") int limit
	);

	@Query(value = """
		SELECT COUNT(DISTINCT p.post_id)
		FROM posts p
		WHERE p.user_id = :userId
		  AND p.is_deleted = false
		  AND (
		    p.title LIKE CONCAT('%', :keyword, '%')
		    OR
		    EXISTS (SELECT 1 FROM contents c WHERE c.post_id = p.post_id AND c.body LIKE CONCAT('%', :keyword, '%'))
		    OR
		    EXISTS (SELECT 1 FROM post_tags pt INNER JOIN tags t ON pt.tag_id = t.tag_id WHERE pt.post_id = p.post_id AND t.name LIKE CONCAT('%', :keyword, '%'))
		  )
		""", nativeQuery = true)
	long countSearchResults(
		@Param("userId") Long userId,
		@Param("keyword") String keyword
	);
}