package troublog.backend.domain.statistics.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import troublog.backend.domain.statistics.dto.response.StatsResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.TagType;

@Repository
public interface StatisticsRepository extends JpaRepository<Post, Long> {

	@Query(value =
		"SELECT date, COUNT(DISTINCT post_id) AS count " +
			"FROM (" +
			"   SELECT p.post_id AS post_id, DATE(p.updated_at) AS date " +
			"   FROM posts p " +
			"   WHERE p.user_id = :userId AND p.updated_at BETWEEN :start AND :end " +
			"   UNION " +
			"   SELECT p.post_id AS post_id, DATE(p.completed_at) AS date " +
			"   FROM posts p " +
			"   WHERE p.user_id = :userId AND p.completed_at BETWEEN :start AND :end " +
			") AS combined " +
			"GROUP BY date " +
			"ORDER BY date",
		nativeQuery = true)
	List<Object[]> findDailyPostCountByUser(
		@Param("userId") Long userId,
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end
	);

	@Query("SELECT new troublog.backend.domain.statistics.dto.response.StatsResDto(t.name, COUNT(t)) " +
		"FROM PostTag pt JOIN pt.post p JOIN pt.tag t " +
		"WHERE p.user.id = :userId AND t.tagType = :tagType " +
		"GROUP BY t.name " +
		"ORDER BY COUNT(t) DESC")
	List<StatsResDto> findTopTagsByUser(@Param("userId") Long userId,
		@Param("tagType") TagType tagType);

	@Query(
		"SELECT new troublog.backend.domain.statistics.dto.response.StatsResDto(CAST(ps.summaryType AS string), COUNT(ps)) "
			+
			"FROM PostSummary ps " +
			"WHERE ps.post.user.id = :userId " +
			"GROUP BY CAST(ps.summaryType AS string) " +
			"ORDER BY COUNT(ps) DESC")
	List<StatsResDto> findSummaryTypesByUser(@Param("userId") Long userId);

}
