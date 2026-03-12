package troublog.backend.domain.trouble.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.user.entity.User;

public interface PostRepository extends JpaRepository<Post, Long> {

	@Query(value = """
		SELECT p.*
		FROM (
			SELECT DISTINCT
				p.post_id,
				(MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE) * 2 +
					IFNULL(MAX(MATCH(c.body) AGAINST(:keyword IN NATURAL LANGUAGE MODE)), 0)) AS total_score
			FROM posts p
			LEFT JOIN contents c ON p.post_id = c.post_id
				AND c.deleted_at IS NULL
			LEFT JOIN post_tags pt ON p.post_id = pt.post_id
				AND pt.deleted_at IS NULL
			LEFT JOIN tags t ON pt.tag_id = t.tag_id
			WHERE p.user_id = :userId
			AND p.deleted_at IS NULL
			AND (
				MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
				OR MATCH(c.body) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
				OR t.tag_name LIKE CONCAT('%', :keyword, '%')
			)
			GROUP BY p.post_id, MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
			ORDER BY total_score DESC, p.post_id DESC
		) ranked_posts
		JOIN posts p ON p.post_id = ranked_posts.post_id
		ORDER BY ranked_posts.total_score DESC, p.post_id DESC
		""", countQuery = """
		SELECT COUNT(DISTINCT p.post_id)
		FROM posts p
		LEFT JOIN contents c ON p.post_id = c.post_id
			AND c.deleted_at IS NULL
		LEFT JOIN post_tags pt ON p.post_id = pt.post_id
			AND pt.deleted_at IS NULL
		LEFT JOIN tags t ON pt.tag_id = t.tag_id
		WHERE p.user_id = :userId
			AND p.deleted_at IS NULL
			AND (
			MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
			OR MATCH(c.body) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
			OR t.tag_name LIKE CONCAT('%', :keyword, '%')
			)
		""", nativeQuery = true)
	Page<Post> searchUserPostsByKeyword(
		@Param("userId") Long userId,
		@Param("keyword") String keyword,
		Pageable pageable
	);

	@SuppressWarnings("checkstyle:RegexpSinglelineJava")
	@Query(value = """
		SELECT p.*
		FROM (
			SELECT DISTINCT
					p.post_id,
					(MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE) * 2 +
					IFNULL(MAX(MATCH(c.body) AGAINST(:keyword IN NATURAL LANGUAGE MODE)), 0)) as total_score
			FROM posts p
			LEFT JOIN contents c ON p.post_id = c.post_id
				AND c.deleted_at IS NULL
			LEFT JOIN post_tags pt ON p.post_id = pt.post_id
				AND pt.deleted_at IS NULL
			LEFT JOIN tags t ON pt.tag_id = t.tag_id
			WHERE p.deleted_at IS NULL
				AND p.visible = true
				AND (
				MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
				OR MATCH(c.body) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
				OR t.tag_name LIKE CONCAT('%', :keyword, '%')
				)
			GROUP BY p.post_id, MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
			ORDER BY total_score DESC, p.post_id DESC
		) ranked_posts
		JOIN posts p ON p.post_id = ranked_posts.post_id
		ORDER BY ranked_posts.total_score DESC, p.post_id DESC
		""", countQuery = """
		SELECT COUNT(DISTINCT p.post_id)
		FROM posts p
		LEFT JOIN contents c ON p.post_id = c.post_id
			AND c.deleted_at IS NULL
		LEFT JOIN post_tags pt ON p.post_id = pt.post_id
			AND pt.deleted_at IS NULL
		LEFT JOIN tags t ON pt.tag_id = t.tag_id
		WHERE p.deleted_at IS NULL
			AND p.visible = true
			AND (
			MATCH(p.title) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
			OR MATCH(c.body) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
			OR t.tag_name LIKE CONCAT('%', :keyword, '%')
			)
		""", nativeQuery = true)
	Page<Post> searchPostsByKeyword(@Param("keyword") String keyword, Pageable pageable);

	@Query("""
		SELECT p
		FROM Post p
		WHERE p.project.id = :projectId
		AND p.status IN :statuses
		AND (:visible IS NULL OR p.isVisible = :visible)
		ORDER BY COALESCE(p.completedAt, p.updatedAt) DESC, p.id DESC
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

	Page<Post> findAllByUser_Id(Long userId, Pageable page);

	@Query(value = """
		SELECT p FROM Post p
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
		""", countQuery = """
		SELECT count(p) FROM Post p
		""")
	Page<Post> findTroublesByUserOrderByStarRating(@Param("userId") Long userId, Pageable pageable);

	@Query("""
			SELECT DISTINCT p
			FROM Post p
			WHERE p.isVisible = TRUE
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
		""")
	List<Post> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") PostStatus status);

	List<Post> findByUserId(Long userId);

	List<Post> findByIdIn(Collection<Long> recentIds);

	List<Post> findAllByUser(User user);

	@Modifying
	@Query(
		value = "DELETE FROM posts WHERE post_id = :postId",
		nativeQuery = true
	)
	void hardDeletePost(@Param("postId") Long postId);

	@Modifying
	@Query(
		value = """
			DELETE FROM posts
			WHERE deleted_at IS NOT NULL
				AND deleted_at <= :threshold
			""",
		nativeQuery = true
	)
	int deleteAllSoftDeletedBefore(@Param("threshold") LocalDateTime threshold);

	@Query(value = "SELECT * FROM posts WHERE deleted_at IS NOT NULL", nativeQuery = true)
	List<Post> findAllDeletedPosts();

	@Query(value = "SELECT p FROM Post p WHERE p.id = :postId AND p.deletedAt IS NOT NULL")
	Optional<Post> findDeletedPostById(@Param("postId") Long postId);

	@Modifying
	@Query(value = "UPDATE posts SET deleted_at = NULL WHERE post_id = :postId", nativeQuery = true)
	void restorePost(@Param("postId") Long postId);
}
