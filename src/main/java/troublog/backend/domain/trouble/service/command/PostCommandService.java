package troublog.backend.domain.trouble.service.command;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
		log.info("[Post] 트러블슈팅 문서 삭제: postId={}, title={}", post.getId(), post.getTitle());
		postRepository.delete(post);
	}

	public void softDeleteAll(List<Post> postList) {
		if(CollectionUtils.isEmpty(postList)) {
			log.info("[Post] 삭제할 게시글 없음");
			return;
		}
		log.info("[Post] 게시글 리스트 soft delete: postList={}", postList);
		postList.forEach(Post::markAsDeleted);
	}
}