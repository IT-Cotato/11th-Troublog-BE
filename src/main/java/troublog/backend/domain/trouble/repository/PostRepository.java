package troublog.backend.domain.trouble.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.ContentSummaryType;
import troublog.backend.domain.trouble.enums.PostStatus;

public interface PostRepository extends JpaRepository<Post, Long> {
	Optional<Post> findByIdAndIsDeletedFalse(Long id);

	List<Post> findByIsDeletedFalse();

	List<Post> findByIsDeletedTrue();

	@Query(value = """
		SELECT DISTINCT p.*
		FROM posts p
		LEFT JOIN contents c ON p.post_id = c.post_id
		LEFT JOIN post_tags pt ON p.post_id = pt.post_id
		LEFT JOIN tags t ON pt.tag_id = t.tag_id
		WHERE p.user_id = :userId
		  AND p.is_deleted = false
		  AND (
		    MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
		    OR MATCH(c.body) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
		    OR t.name LIKE CONCAT('%', :keyword, '%')
		  )
		ORDER BY (
		  MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE) * 2 +
		  MATCH(c.body) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
		) DESC, p.post_id DESC
		""",
		nativeQuery = true)
	Page<Post> searchUserPostsByKeyword(
		@Param("userId") Long userId,
		@Param("keyword") String keyword,
		Pageable pageable
	);

	@Query(value = """
		SELECT DISTINCT p.*
		FROM posts p
		LEFT JOIN contents c ON p.post_id = c.post_id
		LEFT JOIN post_tags pt ON p.post_id = pt.post_id
		LEFT JOIN tags t ON pt.tag_id = t.tag_id
		  WHERE p.is_deleted = false
		  AND (
		    MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
		    OR MATCH(c.body) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
		    OR t.name LIKE CONCAT('%', :keyword, '%')
		  )
		ORDER BY (
		  MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE) * 2 +
		  MATCH(c.body) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
		) DESC, p.post_id DESC
		""",
		nativeQuery = true)
	Page<Post> searchPostsByKeyword(
		@Param("keyword") String keyword,
		Pageable pageable
	);

	@Query("SELECT p FROM Post p JOIN FETCH p.contents c WHERE c.authorType = 'AI_GENERATED' AND c.summaryType = :summaryType AND p.id = :id AND p.isDeleted = false")
	Optional<Post> findSummaryById(@Param("id") Long id, @Param("summaryType") ContentSummaryType summaryType);

	@Query("SELECT p FROM Post p JOIN FETCH p.contents c WHERE c.authorType = 'USER_WRITTEN' AND p.id = :id AND p.isDeleted = false")
	Optional<Post> findPostWithOutSummaryById(@Param("id") Long id);
	
	List<Post> findByUserIdAndStatusAndIsDeletedFalse(Long userId, PostStatus status);
}