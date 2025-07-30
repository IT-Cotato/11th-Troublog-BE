package troublog.backend.domain.trouble.service.command;

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
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTagCommandService {

	private final PostTagRepository postTagRepository;

	public PostTag save(PostTag postTag) {
		log.info("[PostTag] 트러블슈팅 문서 태그 저장: postId={}, tagId={}", postTag.getPost().getId(), postTag.getTag().getId());
		return postTagRepository.save(postTag);
	}

	public List<PostTag> saveAll(List<PostTag> postTags) {
		log.info("[PostTag] 트러블슈팅 문서 태그 일괄 저장: postTagSize={}", postTags.size());
		return postTagRepository.saveAll(postTags);
	}

	public void deleteAll(List<PostTag> postTags) {
		log.info("[PostTag] 트러블슈팅 문서 태그 일괄 삭제: postTagSize={}", postTags.size());
		postTagRepository.deleteAll(postTags);
	}
}
