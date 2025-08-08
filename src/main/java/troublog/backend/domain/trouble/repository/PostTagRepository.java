package troublog.backend.domain.trouble.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import troublog.backend.domain.trouble.entity.PostTag;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
	/**
	 * Fetch Join을 사용하여 PostTag와 Tag를 한번에 조회
	 * N+1 문제 해결 및 성능 최적화
	 */
	@Query("SELECT pt FROM PostTag pt JOIN FETCH pt.tag WHERE pt.post.id = :postId")
	List<PostTag> findAllByPostIdWithTag(@Param("postId") Long postId);

	/**
	 * 태그 이름만 필요한 경우를 위한 DTO Projection
	 * 메모리 효율성 극대화 - 가장 권장하는 방법
	 */
	@Query("SELECT t.name FROM PostTag pt JOIN pt.tag t WHERE pt.post.id = :postId")
	List<String> findTagNamesByPostId(@Param("postId") Long postId);
}