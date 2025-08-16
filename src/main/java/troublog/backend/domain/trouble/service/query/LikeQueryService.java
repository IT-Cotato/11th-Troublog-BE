package troublog.backend.domain.trouble.service.query;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public Page<Like> findByUserIdOrderByLikedAtDesc(Long userId, Pageable pageable) {
		return likeRepository.findByUserIdOrderByLikedAtDesc(userId, pageable);
	}

	public int countByPostId(Long postId) {
		return likeRepository.countByPostId(postId);
	}
}
