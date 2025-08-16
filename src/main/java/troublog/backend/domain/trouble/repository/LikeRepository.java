package troublog.backend.domain.trouble.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import troublog.backend.domain.trouble.entity.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
	Page<Like> findByUserIdOrderByLikedAtDesc(Long userId, Pageable pageable);

	Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

	long countByPostId(Long postId);
}
