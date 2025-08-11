package troublog.backend.domain.trouble.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.Like;
import troublog.backend.domain.trouble.repository.LikeRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeCommandService {
	private final LikeRepository likeRepository;

	public Like save(Like like) {
		log.info("[Post] 포스트 좋아요 등록: likeId={}, postId={}", like.getId(), like.getPost().getId());
		return likeRepository.save(like);
	}

	public void deleteLike(Like like) {
		log.info("[Post] 포스트 좋아요 취소: likeId={}, postId={}", like.getId(), like.getPost().getId());
		like.getPost().removeLike(like);
		likeRepository.delete(like);
	}
}
