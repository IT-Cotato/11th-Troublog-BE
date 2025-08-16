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
		  AND c.author_type = 'USER_WRITTEN'
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
		countQuery = """
			SELECT COUNT(DISTINCT p.post_id)
			FROM posts p
			LEFT JOIN contents c ON p.post_id = c.post_id
			LEFT JOIN post_tags pt ON p.post_id = pt.post_id
			LEFT JOIN tags t ON pt.tag_id = t.tag_id
			WHERE p.user_id = :userId
			  AND p.is_deleted = false
			  AND c.author_type = 'USER_WRITTEN'
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
		SELECT DISTINCT p.*
		FROM posts p
		LEFT JOIN contents c ON p.post_id = c.post_id
		LEFT JOIN post_tags pt ON p.post_id = pt.post_id
		LEFT JOIN tags t ON pt.tag_id = t.tag_id
		  WHERE p.is_deleted = false
		  AND p.visible = true
		  AND c.author_type = 'USER_WRITTEN'
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
		countQuery = """
			SELECT COUNT(DISTINCT p.post_id)
			FROM posts p
			LEFT JOIN contents c ON p.post_id = c.post_id
			LEFT JOIN post_tags pt ON p.post_id = pt.post_id
			LEFT JOIN tags t ON pt.tag_id = t.tag_id
			WHERE p.is_deleted = false
			  AND p.visible = true
			  AND c.author_type = 'USER_WRITTEN'
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

	@Query("SELECT p FROM Post p JOIN FETCH p.contents c WHERE c.authorType = 'AI_GENERATED' AND c.summaryType = :summaryType AND p.id = :id AND p.isDeleted = false")
	Optional<Post> findSummaryById(@Param("id") Long id, @Param("summaryType") ContentSummaryType summaryType);

	@Query("SELECT p FROM Post p JOIN FETCH p.contents c WHERE c.authorType = 'USER_WRITTEN' AND p.id = :id AND p.isDeleted = false")
	Optional<Post> findPostWithoutSummaryById(@Param("id") Long id);

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
		             select 1 from Content c
		              where c.post = p and c.summaryType = :summaryType
		       ))
		""")
	List<Post> findByProjectSummarized(
		@Param("projectId") Long projectId,
		@Param("status") PostStatus status,
		@Param("summaryType") ContentSummaryType summaryType,
		Sort sort
	);

	@Query("""
		    select p
		      from Post p
		     where p.project.id = :projectId
		       and p.isDeleted = false
		       and p.status = :status
		       and (:summaryType is null or exists (
		             select 1 from Content c
		              where c.post = p and c.summaryType = :summaryType
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
		@Param("summaryType") ContentSummaryType summaryType
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