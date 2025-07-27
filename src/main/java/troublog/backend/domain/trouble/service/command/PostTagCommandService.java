package troublog.backend.domain.trouble.service.command;

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
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTagCommandService {

	private final PostTagRepository postTagRepository;

	public PostTag save(PostTag postTag) {
		return postTagRepository.save(postTag);
	}

	public List<PostTag> saveAll(List<PostTag> postTags) {
		return postTagRepository.saveAll(postTags);
	}

	public void deleteAll(List<PostTag> postTags) {
		postTagRepository.deleteAll(postTags);
	}
}
