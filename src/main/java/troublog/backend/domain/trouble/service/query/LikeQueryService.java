package troublog.backend.domain.trouble.service.query;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.Like;
import troublog.backend.domain.trouble.repository.LikeRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeQueryService {

	private final LikeRepository likeRepository;

	public Optional<Like> findByUserAndPost(Long userId, Long postId) {
		return likeRepository.findByUserIdAndPostId(userId, postId);
	}

	@Transactional(readOnly = true)
	public List<Like> findByUserIdOrderByLikedAtDesc(Long userId) {
		return likeRepository.findByUserIdOrderByLikedAtDesc(userId);
	}
}
