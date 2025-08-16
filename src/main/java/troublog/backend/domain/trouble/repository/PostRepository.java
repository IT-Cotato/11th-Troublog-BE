package troublog.backend.domain.trouble.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.SummaryType;
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
		ORDER BY ranked_posts.total_score DESC, p.post_id DESC;
		
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
		    select p
		      from Post p
		     where p.project.id = :projectId
		       and p.isDeleted = false
		       and p.status = :status
		       and (:visible is null or p.isVisible = :visible)
		""")
	List<Post> findByProjectCompleted(
		@Param("projectId") Long projectId,
		@Param("status") PostStatus status,
		@Param("visible") Boolean visible,
		Sort sort
	);

	@Query("""
		    select p
		      from Post p
		     where p.project.id = :projectId
		       and p.isDeleted = false
		       and p.status = :status
		       and (:visible is null or p.isVisible = :visible)
		     order by
		       case p.starRating
		         when troublog.backend.domain.trouble.enums.StarRating.FIVE_STARS  then 5
		         when troublog.backend.domain.trouble.enums.StarRating.FOUR_STARS  then 4
		         when troublog.backend.domain.trouble.enums.StarRating.THREE_STARS then 3
		         when troublog.backend.domain.trouble.enums.StarRating.TWO_STARS   then 2
		         when troublog.backend.domain.trouble.enums.StarRating.ONE_STAR    then 1
		         else 0
		       end desc,
		       p.id desc
		""")
	List<Post> findByProjectCompletedImportant(
		@Param("projectId") Long projectId,
		@Param("status") PostStatus status,
		@Param("visible") Boolean visible
	);

	@Query("""
		    select p
		      from Post p
		     where p.project.id = :projectId
		       and p.isDeleted = false
		       and p.status = :status
		       and (:summaryType is null or exists (
		             select 1 from PostSummary ps
		              where ps.post = p and ps.summaryType = :summaryType
		       ))
		""")
	List<Post> findByProjectSummarized(
		@Param("projectId") Long projectId,
		@Param("status") PostStatus status,
		@Param("summaryType") SummaryType summaryType,
		Sort sort
	);

	@Query("""
		    select p
		      from Post p
		     where p.project.id = :projectId
		       and p.isDeleted = false
		       and p.status = :status
		       and (:summaryType is null or exists (
		             select 1 from PostSummary ps
		              where ps.post = p and ps.summaryType = :summaryType
		       ))
		     order by
		       case p.starRating
		         when troublog.backend.domain.trouble.enums.StarRating.FIVE_STARS  then 5
		         when troublog.backend.domain.trouble.enums.StarRating.FOUR_STARS  then 4
		         when troublog.backend.domain.trouble.enums.StarRating.THREE_STARS then 3
		         when troublog.backend.domain.trouble.enums.StarRating.TWO_STARS   then 2
		         when troublog.backend.domain.trouble.enums.StarRating.ONE_STAR    then 1
		         else 0
		       end desc,
		       p.id desc
		""")
	List<Post> findByProjectSummarizedImportant(
		@Param("projectId") Long projectId,
		@Param("status") PostStatus status,
		@Param("summaryType") SummaryType summaryType
	);

	Page<Post> findAllByUser_IdAndIsDeletedFalse(Long userId, Pageable page);

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