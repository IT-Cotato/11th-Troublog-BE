package troublog.backend.domain.trouble.service.command;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.repository.PostRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCommandService {

	private final PostRepository postRepository;

	public Post save(Post post) {
		log.info("[Post] 트러블슈팅 문서 저장: postId={}, title={}", post.getId(), post.getTitle());
		return postRepository.save(post);
	}

	public void delete(Post post) {
		log.info("[Post] 트러블슈팅 문서 삭제: postId={}, title={}", post.getId(), post.getTitle());
		postRepository.delete(post);
	}
}