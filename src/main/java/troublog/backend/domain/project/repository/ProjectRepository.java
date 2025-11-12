package troublog.backend.domain.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
	List<String> findTop2TagsByProjectId(@Param("projectId") Long projectId, @Param("tagType") TagType tagType,
		Pageable pageable);

	Page<Project> findAllByUserIdAndIsDeletedFalse(Long userId, Pageable pageable);

	List<Project> findAllByUserAndIsDeletedFalse(User user);
}