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
import troublog.backend.domain.trouble.dto.request.PostUpdateReqDto;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.entity.Content;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.enums.StarRating;
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

	public PostResDto updatePost(long postId, PostUpdateReqDto reqDto) {
		Post foundPost = postQueryService.findPostById(postId);
		updateCommonInfo(reqDto, foundPost);
		updateProjectRelations(foundPost, reqDto.projectId());
		updateContentRelations(foundPost, reqDto.contentDtoList());
		updatePostTagRelations(foundPost, reqDto.errorTagName(), reqDto.postTags());
		//TODO Image URL(String) -> PostImage 변환후 연관관계 메서드 호출 필요

		Post savedPost = postRepository.save(foundPost);
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

	public PostResDto updatePost(long postId, PostUpdateReqDto reqDto) {
		Post foundPost = postQueryService.findPostById(postId);
		updateCommonInfo(reqDto, foundPost);
		updateProjectRelations(foundPost, reqDto.projectId());
		updateContentRelations(foundPost, reqDto.contentDtoList());
		updatePostTagRelations(foundPost, reqDto.errorTagName(), reqDto.postTags());
		//TODO Image URL(String) -> PostImage 변환후 연관관계 메서드 호출 필요

		Post savedPost = postRepository.save(foundPost);
		return PostConverter.toResponse(savedPost);
	}	public PostResDto updatePost(long postId, PostUpdateReqDto reqDto) {
		Post foundPost = postQueryService.findPostById(postId);
		updateCommonInfo(reqDto, foundPost);
		updateProjectRelations(foundPost, reqDto.projectId());
		updateContentRelations(foundPost, reqDto.contentDtoList());
		updatePostTagRelations(foundPost, reqDto.errorTagName(), reqDto.postTags());
		//TODO Image URL(String) -> PostImage 변환후 연관관계 메서드 호출 필요

		Post savedPost = postRepository.save(foundPost);
		return PostConverter.toResponse(savedPost);
	}private static void updateCommonInfo(PostUpdateReqDto reqDto, Post foundPost) {
		foundPost.updateTitle(reqDto.title());
		foundPost.updateIntroduction(reqDto.introduction());
		foundPost.updateVisibility(reqDto.isVisible());
		foundPost.updateStatus(PostStatus.from(reqDto.postStatus()));
		foundPost.updateStarRating(StarRating.from(reqDto.starRating()));
	}

	private void updateProjectRelations(Post post, int newProjectId) {
		if (post.getProject().getId() == newProjectId) {
			return;
		}
		setProjectRelations(post, newProjectId);
	}

	private void updateContentRelations(Post post, List<ContentDto> contentDtoList) {
		post.getContents().clear();
		setContentRelations(post, contentDtoList);
	}

	private void updatePostTagRelations(Post post, String errorTagName, List<String> techStackTags) {
		post.getPostTags().clear();
		postTagCommandService.deleteAllTagByPostId(post.getId());
		setErrorTagRelations(post, errorTagName);
		setTechStackTagRelations(post, techStackTags);
	}

	private void updatePostImageRelations(Post post, List<String> postImages) {
		// 기존 이미지 관계 정리
		post.getPostImages().clear();
		// 새로운 이미지 관계 설정 (기존 메서드 재사용)
		setPostImageRelations(post, postImages);
	}
}