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
		log.info("[Post] like 등록: userId={}, postId={}", like.getUser().getId(), like.getPost().getId());
		return likeRepository.save(like);
	}

	public void deleteLike(Like like) {
		log.info("[Post] like 취소: userId={}, postId={}", like.getUser().getId(), like.getPost().getId());
		like.getPost().removeLike(like);
		like.getUser().removeLikeRef(like);
		likeRepository.delete(like);
	}
}
