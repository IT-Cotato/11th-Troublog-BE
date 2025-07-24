package troublog.backend.domain.trouble.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.repository.PostRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostQueryService {
	private final PostRepository postRepository;

	public Post findPostById(long id) {
		return postRepository.findById(id)
			.orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
	}
}