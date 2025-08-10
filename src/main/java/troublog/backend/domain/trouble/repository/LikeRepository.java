package troublog.backend.domain.trouble.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import troublog.backend.domain.trouble.entity.Like;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.user.entity.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
	List<Like> findByUserIdOrderByLikedAtDesc(Long userId);

	Boolean existsByUserAndPost(User user, Post post);
}
