package troublog.backend.domain.trouble.service.command;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.project.service.query.ProjectQueryService;
import troublog.backend.domain.trouble.converter.PostConverter;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.dto.resquest.PostCreateReqDto;
import troublog.backend.domain.trouble.entity.Content;
import troublog.backend.domain.trouble.entity.ErrorTag;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.repository.PostRepository;
import troublog.backend.domain.trouble.repository.PostTagRepository;
import troublog.backend.domain.trouble.repository.TechStackRepository;
import troublog.backend.domain.trouble.service.query.ErrorTagQueryService;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.service.UserQueryService;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCommandService {

	private final PostRepository postRepository;
	private final UserQueryService userQueryService;
	private final ProjectQueryService projectQueryService;
	private final ContentCommandService contentCommandService;
	private final ErrorTagQueryService errorTagQueryService;
	private final PostTagCommandService postTagCommandService;
	private final TechStackRepository techStackRepository;
	private final PostTagRepository postTagRepository;

	public PostResDto createPost(PostCreateReqDto reqDto, String email) {
		Post newPost = PostConverter.toEntity(reqDto);
		User user = userQueryService.findUserByEmail(email);
		Project project = projectQueryService.findProjectById(reqDto.projectId());
		List<Content> contents = contentCommandService.saveAllContent(reqDto.contentDtoList());
		List<PostTag> postTags = postTagCommandService.savePostTags(reqDto.postTags(), newPost);
		ErrorTag errorTag = errorTagQueryService.findErrorTagById(reqDto.errorTagId());
		Post savedPost = postRepository.save(newPost);
		//TODO 연관관계 설정
		//TODO 이미지 업로드 후 연관관계 설정

		return PostConverter.toResponse(savedPost);
	}
}
