package troublog.backend.domain.trouble.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.enums.SummaryType;

public interface PostSummaryRepository extends JpaRepository<PostSummary, Long> {
	@Query("""
		    select ps
		      from PostSummary ps
		     where ps.post.project.id = :projectId
		       and ps.post.isDeleted = false
		       and (:status is null or ps.post.status = :status)  
		       and (:summaryType is null or ps.summaryType = :summaryType)
		""")
	List<PostSummary> findByProjectSummarized(
		@Param("projectId") Long projectId,
		@Param("status") PostStatus status,
		@Param("summaryType") SummaryType summaryType,
		Sort sort
	);

	@EntityGraph(attributePaths = {"post", "post.project"})
	@Query("""
		select ps
		from PostSummary ps
		where ps.post.project.id = :projectId
		  and coalesce(ps.post.isDeleted, false) = false
		  and (:status is null or ps.post.status = :status)
		  and (:summaryType is null or ps.summaryType = :summaryType)
		order by
		  case
		    when ps.post.starRating = troublog.backend.domain.trouble.enums.StarRating.FIVE_STARS  then 5
		    when ps.post.starRating = troublog.backend.domain.trouble.enums.StarRating.FOUR_STARS  then 4
		    when ps.post.starRating = troublog.backend.domain.trouble.enums.StarRating.THREE_STARS then 3
		    when ps.post.starRating = troublog.backend.domain.trouble.enums.StarRating.TWO_STARS   then 2
		    when ps.post.starRating = troublog.backend.domain.trouble.enums.StarRating.ONE_STAR    then 1
		    else 0
		  end desc,
		  ps.id desc
		""")
	List<PostSummary> findByProjectSummarizedImportant(
		@Param("projectId") Long projectId,
		@Param("status") PostStatus status,               // null 허용
		@Param("summaryType") SummaryType summaryType     // null 허용
	);
}