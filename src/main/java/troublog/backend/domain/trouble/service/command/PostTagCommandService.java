package troublog.backend.domain.trouble.service.command;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.entity.TechStack;
import troublog.backend.domain.trouble.repository.PostTagRepository;
import troublog.backend.domain.trouble.repository.TechStackRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTagCommandService {
	private final TechStackRepository techStackRepository;
	private final PostTagRepository postTagRepository;

	public List<PostTag> savePostTags(List<String> tagNames, Post post) {
		List<TechStack> existingTechStacks = techStackRepository.findByNameIn(tagNames);

		List<PostTag> postTags = existingTechStacks.stream()
			.map(techStack -> PostTag.builder()
				.post(post)
				.techStack(techStack)
				.build())
			.collect(Collectors.toList());

		return postTagRepository.saveAll(postTags);
	}
}