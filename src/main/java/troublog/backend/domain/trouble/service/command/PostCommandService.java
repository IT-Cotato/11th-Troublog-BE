package troublog.backend.domain.trouble.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.repository.PostRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCommandService {

	private final PostRepository postRepository;

	public Post savePost(final Post post) {
		log.info("[Post] 트러블슈팅 문서 저장: postId={}, title={}", post.getId(), post.getTitle());
		return postRepository.save(post);
	}

	public void deletePost(final Post post) {
		log.info("[Post] 트러블슈팅 문서 hard delete: postId={}, title={}", post.getId(), post.getTitle());
		postRepository.hardDeletePost(post.getId());
	}

	public void softDelete(final Post post) {
		log.info("[Post] 트러블슈팅 문서 soft delete: postId={}, title={}", post.getId(), post.getTitle());
		postRepository.delete(post);
	}

	public void restore(final Post foundPost) {
		log.info("[Post] 트러블슈팅 문서 복원: postId={}, title={}", foundPost.getId(), foundPost.getTitle());
		postRepository.restorePost(foundPost.getId());
	}
}
