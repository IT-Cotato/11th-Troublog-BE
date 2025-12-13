package troublog.backend.domain.project.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.trouble.enums.TagType;
import troublog.backend.domain.user.entity.User;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	@Query("""
		SELECT t.name
		FROM Post p
		JOIN PostTag pt ON p.id = pt.post.id
		JOIN Tag t ON pt.tag.id = t.id
		WHERE p.project.id = :projectId
		AND t.tagType = :tagType
		GROUP BY t.name
		ORDER BY COUNT(t.name) DESC
		""")
	List<String> findTop2TagsByProjectId(
		@Param("projectId") Long projectId,
		@Param("tagType") TagType tagType,
		Pageable pageable
	);

	Page<Project> findAllByUserId(Long userId, Pageable pageable);

	List<Project> findAllByUser(User user);

	@Modifying
	@Query(value = "DELETE FROM projects WHERE deleted_at IS NOT NULL AND deleted_at <= :threshold", nativeQuery = true)
	int deleteAllSoftDeletedBefore(@Param("threshold") LocalDateTime threshold);

}
