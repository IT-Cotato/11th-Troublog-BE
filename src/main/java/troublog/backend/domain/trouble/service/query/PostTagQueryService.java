package troublog.backend.domain.trouble.service.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.repository.PostTagRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTagQueryService {
	private final PostTagRepository postTagRepository;

	public List<PostTag> findAllByPostId(Long postId) {
		return postTagRepository.findAllByPostId(postId);
	}
}