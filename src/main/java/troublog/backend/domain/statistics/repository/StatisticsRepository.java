package troublog.backend.domain.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import troublog.backend.domain.statistics.dto.response.StatsResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.TagType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT DATE(p.completed_at) AS date, COUNT(*) AS count " +
            "FROM posts p " +
            "WHERE p.user_id = :userId AND p.completed_at BETWEEN :start AND :end " +
            "GROUP BY DATE(p.completed_at) " +
            "ORDER BY DATE(p.completed_at)", nativeQuery = true)
    List<Object[]> findDailyPostCountByUser(@Param("userId") Long userId,
                                            @Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end);


    @Query("SELECT new troublog.backend.domain.statistics.dto.response.StatsResDto(t.name, COUNT(t)) " +
            "FROM PostTag pt JOIN pt.post p JOIN pt.tag t " +
            "WHERE p.user.id = :userId AND t.tagType = :tagType " +
            "GROUP BY t.name " +
            "ORDER BY COUNT(t) DESC")
    List<StatsResDto> findTopTagsByUser(@Param("userId") Long userId,
                                               @Param("tagType") TagType tagType);


    @Query("SELECT new troublog.backend.domain.statistics.dto.response.StatsResDto(STR(ps.summaryType), COUNT(ps)) " +
            "FROM PostSummary ps " +
            "WHERE ps.post.user.id = :userId " +
            "GROUP BY STR(ps.summaryType) " +
            "ORDER BY COUNT(ps) DESC")
    List<StatsResDto> findSummaryTypesByUser(@Param("userId") Long userId);

}
