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
import troublog.backend.domain.trouble.dto.request.ContentDto;
import troublog.backend.domain.trouble.dto.request.PostCreateReqDto;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.entity.Content;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.repository.PostRepository;
import troublog.backend.domain.trouble.service.factory.PostFactory;
import troublog.backend.domain.trouble.service.query.PostQueryService;
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
	private final PostTagCommandService postTagCommandService;
	private final PostQueryService postQueryService;
	private final PostFactory postFactory;

	public PostResDto createPost(PostCreateReqDto reqDto, long userId) {
		Post newPost = postFactory.createPost(reqDto);
		setUserRelations(newPost, userId);
		setProjectRelations(newPost, reqDto.projectId());
		setErrorTagRelations(newPost, reqDto.errorTagName());
		Post savedPost = postRepository.save(newPost);

		setContentRelations(savedPost, reqDto.contentDtoList());
		setTechStackTagRelations(savedPost, reqDto.postTags());
		//TODO Image URL(String) -> PostImage 변환후 연관관계 메서드 호출 필요

		return PostConverter.toResponse(savedPost);
	}

	public void deletePost(long postId) {
		Post foundPost = postQueryService.findPostById(postId);
		postRepository.delete(foundPost);
	}

	private void setProjectRelations(Post post, int projectId) {
		Project project = projectQueryService.findProjectById(projectId);
		project.addPost(post);
	}

	private void setUserRelations(Post post, long userId) {
		User user = userQueryService.findUserById(userId);
		user.addPost(post);
	}

	private void setErrorTagRelations(Post post, String tagName) {
		PostTag postTag = postTagCommandService.saveErrorPostTag(tagName, post);
		post.addPostTag(postTag);
	}

	private void setContentRelations(Post post, List<ContentDto> contentDtoList) {
		if (hasContents(contentDtoList)) {
			List<Content> contents = contentCommandService.saveAllContent(contentDtoList);
			contents.forEach(post::addContent);
		}
	}

	private void setTechStackTagRelations(Post post, List<String> reqTechStackTags) {
		if (hasTechStackTag(reqTechStackTags)) {
			List<PostTag> techStackPostTags = postTagCommandService.saveTechStackPostTags(reqTechStackTags, post);
			techStackPostTags.forEach(post::addPostTag);
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

	private boolean hasTechStackTag(List<String> postTags) {
		return postTags != null && !postTags.isEmpty();
	}

	private boolean hasPostImages(List<String> postImages) {
		return postImages != null && !postImages.isEmpty();
	}
}