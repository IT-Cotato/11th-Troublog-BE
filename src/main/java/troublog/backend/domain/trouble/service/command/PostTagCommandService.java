package troublog.backend.domain.trouble.service.command;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.entity.Tag;
import troublog.backend.domain.trouble.repository.PostTagRepository;
import troublog.backend.domain.trouble.service.query.PostTagQueryService;
import troublog.backend.domain.trouble.service.query.TagQueryService;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTagCommandService {

	private final TagQueryService tagQueryService;
	private final PostTagRepository postTagRepository;
	private final PostTagQueryService postTagQueryService;

	public PostTag saveErrorPostTag(String tagName, Post post) {
		Tag errorTag = tagQueryService.findErrorTagByName(tagName);
		PostTag postTag = createPostTag(errorTag, post);
		return postTagRepository.save(postTag);
	}

	public List<PostTag> saveTechStackPostTags(List<String> tagNames, Post post) {
		List<Tag> techStackTags = tagQueryService.findTechStackTagsByNames(tagNames);
		List<PostTag> postTags = techStackTags.stream()
			.map(tag -> createPostTag(tag, post))
			.toList();
		return postTagRepository.saveAll(postTags);
	}

	public void deleteAllTagByPostId(Long postId) {
		List<PostTag> postTags = postTagQueryService.findPostTagsByPostId(postId);
		postTagRepository.deleteAll(postTags);
	}

	private PostTag createPostTag(Tag tag, Post post) {
		PostTag postTag = PostTag.builder().build();
		postTag.assignPost(post);
		postTag.assignTag(tag);
		return postTag;
	}
}
