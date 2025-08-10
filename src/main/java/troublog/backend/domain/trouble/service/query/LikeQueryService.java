package troublog.backend.domain.trouble.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.repository.LikeRepository;
import troublog.backend.domain.user.entity.User;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeQueryService {

	private final LikeRepository likeRepository;

	public void assertLikeNotExists(User user, Post post) {
		if (likeRepository.existsByUserAndPost(user, post)) {
			throw new PostException(ErrorCode.LIKE_ALREADY_EXISTS);
		}
	}
}
