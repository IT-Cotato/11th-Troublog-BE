package troublog.backend.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import troublog.backend.domain.like.entity.Like;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUserIdOrderByLikedAtDesc(Long userId);
}
