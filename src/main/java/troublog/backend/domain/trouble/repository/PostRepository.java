package troublog.backend.domain.trouble.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.PostStatus;

public interface PostRepository extends JpaRepository<Post, Long> {
	Optional<Post> findByIdAndIsDeletedFalse(Long id);

	List<Post> findByIsDeletedFalse();

	List<Post> findByIsDeletedTrue();

	@Query(value = """
		SELECT p.*
		FROM (
		  SELECT DISTINCT
		         p.post_id,
		         (MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE) * 2 +
		          IFNULL(MAX(MATCH(c.body) AGAINST(:keyword IN NATURAL LANGUAGE MODE)), 0)) AS total_score
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
		  GROUP BY p.post_id, MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
		  ORDER BY total_score DESC, p.post_id DESC
		) ranked_posts
		JOIN posts p ON p.post_id = ranked_posts.post_id
		ORDER BY ranked_posts.total_score DESC, p.post_id DESC
		
		""",
		countQuery = """
			SELECT COUNT(DISTINCT p.post_id)
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
			""",
		nativeQuery = true)
	Page<Post> searchUserPostsByKeyword(
		@Param("userId") Long userId,
		@Param("keyword") String keyword,
		Pageable pageable
	);

	@Query(value = """
		SELECT p.*
		FROM (
		    SELECT DISTINCT
		           p.post_id,
		           (MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE) * 2 +
		            IFNULL(MAX(MATCH(c.body) AGAINST(:keyword IN NATURAL LANGUAGE MODE)), 0)) as total_score
		    FROM posts p
		    LEFT JOIN contents c ON p.post_id = c.post_id
		    LEFT JOIN post_tags pt ON p.post_id = pt.post_id
		    LEFT JOIN tags t ON pt.tag_id = t.tag_id
		    WHERE p.is_deleted = false
		      AND p.visible = true
		      AND (
		        MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
		        OR MATCH(c.body) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
		        OR t.name LIKE CONCAT('%', :keyword, '%')
		      )
		    GROUP BY p.post_id, MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
		    ORDER BY total_score DESC, p.post_id DESC
		) ranked_posts
		JOIN posts p ON p.post_id = ranked_posts.post_id
		ORDER BY ranked_posts.total_score DESC, p.post_id DESC
		""",
		countQuery = """
			SELECT COUNT(DISTINCT p.post_id)
			FROM posts p
			LEFT JOIN contents c ON p.post_id = c.post_id
			LEFT JOIN post_tags pt ON p.post_id = pt.post_id
			LEFT JOIN tags t ON pt.tag_id = t.tag_id
			WHERE p.is_deleted = false
			  AND p.visible = true
			  AND (
			    MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
			    OR MATCH(c.body) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
			    OR t.name LIKE CONCAT('%', :keyword, '%')
			  )
			""",
		nativeQuery = true)
	Page<Post> searchPostsByKeyword(
		@Param("keyword") String keyword,
		Pageable pageable
	);

	@Query("""
		SELECT p
		FROM Post p
		WHERE p.project.id = :projectId
		AND p.isDeleted = FALSE
		AND p.status IN :statuses
		AND (:visible IS NULL OR p.isVisible = :visible)
		ORDER BY COALESCE(p.completedAt, p.updated_at) DESC, p.id DESC
		""")
	List<Post> findByProjectWithStatuses(
		@Param("projectId") Long projectId,
		@Param("statuses") List<PostStatus> statuses,
		@Param("visible") Boolean visible
	);

	@Query("""
		    SELECT p
		      FROM Post p
		     WHERE p.project.id = :projectId
		       AND p.isDeleted = FALSE
		       AND p.status IN :statuses
		       AND (:visible IS NULL OR p.isVisible = :visible)
		    ORDER BY
		  CASE
		    WHEN p.starRating = troublog.backend.domain.trouble.enums.StarRating.FIVE_STARS  THEN 5
		    WHEN p.starRating = troublog.backend.domain.trouble.enums.StarRating.FOUR_STARS  THEN 4
		    WHEN p.starRating = troublog.backend.domain.trouble.enums.StarRating.THREE_STARS THEN 3
		    WHEN p.starRating = troublog.backend.domain.trouble.enums.StarRating.TWO_STARS   THEN 2
		    WHEN p.starRating = troublog.backend.domain.trouble.enums.StarRating.ONE_STAR    THEN 1
		    ELSE 0
		  END DESC,
		       p.id DESC
		""")
	List<Post> findByProjectImportantWithStatuses(
		@Param("projectId") Long projectId,
		@Param("statuses") List<PostStatus> statuses,
		@Param("visible") Boolean visible
	);

	Page<Post> findAllByUser_IdAndIsDeletedFalse(Long userId, Pageable page);

	@Query(value = """
		SELECT p FROM Post p
		WHERE p.user.id = :userId AND p.isDeleted = false
		ORDER BY
		    CASE p.starRating
		        WHEN troublog.backend.domain.trouble.enums.StarRating.FIVE_STARS THEN 5
		        WHEN troublog.backend.domain.trouble.enums.StarRating.FOUR_STARS THEN 4
		        WHEN troublog.backend.domain.trouble.enums.StarRating.THREE_STARS THEN 3
		        WHEN troublog.backend.domain.trouble.enums.StarRating.TWO_STARS THEN 2
		        WHEN troublog.backend.domain.trouble.enums.StarRating.ONE_STAR THEN 1
		        ELSE 0
		    END DESC,
		    p.id DESC
		""",
		countQuery = """
			SELECT count(p) FROM Post p
			WHERE p.user.id = :userId AND p.isDeleted = false
			""")
	Page<Post> findTroublesByUserOrderByStarRating(@Param("userId") Long userId,
		Pageable pageable);

	@Query("""
		  SELECT DISTINCT p
		    FROM Post p
		   WHERE p.isDeleted = FALSE
		     AND p.isVisible = TRUE
		     AND p.status IN (
		       troublog.backend.domain.trouble.enums.PostStatus.COMPLETED,
		       troublog.backend.domain.trouble.enums.PostStatus.SUMMARIZED
		     )
		""")
	Page<Post> getCommunityPosts(Pageable page);

	@Query("""
		  SELECT p
		    FROM Post p
		   WHERE p.user.id = :userId
		     AND (:status is null or p.status = :status)
		     AND p.isDeleted = FALSE
		""")
	List<Post> findByUserIdAndStatusAndIsDeletedFalse(@Param("userId") Long userId, @Param("status") PostStatus status);

	List<Post> findByUserIdAndIsDeletedFalse(Long userId);

	List<Post> findByIdIn(Collection<Long> recentIds);
}