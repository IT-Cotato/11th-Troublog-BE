package troublog.backend.domain.trouble.service.query;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.repository.PostTagRepository;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTagQueryService {
	private final PostTagRepository postTagRepository;

	public List<PostTag> findAllByPostId(Long postId) {
		List<PostTag> postTags = postTagRepository.findAllByPostId(postId);
		log.info("[PostTag] 게시글별 태그 조회 결과: postId={}, tagCount={}", postId, postTags.size());
		return postTags;
	}
}