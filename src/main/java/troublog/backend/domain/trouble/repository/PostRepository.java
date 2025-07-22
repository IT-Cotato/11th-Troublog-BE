package troublog.backend.domain.trouble.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import troublog.backend.domain.trouble.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	/**
	 * Post와 연관된 모든 엔티티들을 한 번에 조회 (N+1 문제 해결)
	 */
	@Query("SELECT DISTINCT p FROM Post p " +
		   "LEFT JOIN FETCH p.contents c " +
		   "LEFT JOIN FETCH p.postTags pt " +
		   "LEFT JOIN FETCH pt.tag t " +
		   "LEFT JOIN FETCH p.user u " +
		   "LEFT JOIN FETCH p.project pr " +
		   "WHERE p.id = :postId")
	Optional<Post> findByIdWithRelations(@Param("postId") Long postId);
}