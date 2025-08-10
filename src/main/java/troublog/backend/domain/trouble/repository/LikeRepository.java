package troublog.backend.domain.trouble.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import troublog.backend.domain.trouble.entity.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
	List<Like> findByUserIdOrderByLikedAtDesc(Long userId);

	Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
	
}
