package troublog.backend.domain.trouble.service.command;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.project.service.query.ProjectQueryService;
import troublog.backend.domain.trouble.converter.PostConverter;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.dto.resquest.ContentDto;
import troublog.backend.domain.trouble.dto.resquest.PostCreateReqDto;
import troublog.backend.domain.trouble.entity.Content;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.repository.PostRepository;
import troublog.backend.domain.trouble.service.query.ErrorTagQueryService;
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

	public PostResDto createPost(PostCreateReqDto reqDto, String email) {
		Post newPost = PostConverter.toEntity(reqDto);
		setRequiredRelations(newPost, reqDto, email);
		Post savedPost = postRepository.save(newPost);

		setContentRelations(savedPost, reqDto.contentDtoList());
		setPostTagRelations(savedPost, reqDto.postTags());
		//TODO Image URL(String) -> PostImage 변환후 연관관계 메서드 호출 필요

		return PostConverter.toResponse(savedPost);
	}

	private void setRequiredRelations(Post post, PostCreateReqDto reqDto, String email) {
		post.assignUser(userQueryService.findUserByEmail(email));
		post.assignProject(projectQueryService.findProjectById(reqDto.projectId()));
		post.assignErrorTag(errorTagQueryService.findErrorTagById(reqDto.errorTagId()));
	}

	private void setContentRelations(Post post, List<ContentDto> contentDtoList) {
		if (hasContents(contentDtoList)) {
			List<Content> contents = contentCommandService.saveAllContent(contentDtoList);
			post.addContents(contents);
		}
	}

	private void setPostTagRelations(Post post, List<String> reqPostTags) {
		if (hasPostTags(reqPostTags)) {
			List<PostTag> postTags = postTagCommandService.savePostTags(reqPostTags, post);
			post.addPostTags(postTags);
		}
	}

	private void setPostImageRelations(Post post, List<String> postImages) {
		if (hasPostImages(postImages)) {
			//TODO 이미지 저장 로직 구현 필요
			// post.addPostImages(postImages);
		}
	}

	private boolean hasContents(List<ContentDto> contentDtoList) {
		return contentDtoList != null && !contentDtoList.isEmpty();
	}

	private boolean hasPostTags(List<String> postTags) {
		return postTags != null && !postTags.isEmpty();
	}

	private boolean hasPostImages(List<String> postImages) {
		return postImages != null && !postImages.isEmpty();
	}

}
