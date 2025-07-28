package troublog.backend.domain.trouble.service.facade;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.project.service.query.ProjectQueryService;
import troublog.backend.domain.trouble.converter.ContentConverter;
import troublog.backend.domain.trouble.dto.request.ContentDto;
import troublog.backend.domain.trouble.dto.request.PostCreateReqDto;
import troublog.backend.domain.trouble.dto.request.PostUpdateReqDto;
import troublog.backend.domain.trouble.entity.Content;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.entity.Tag;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.enums.StarRating;
import troublog.backend.domain.trouble.service.command.ContentCommandService;
import troublog.backend.domain.trouble.service.command.PostTagCommandService;
import troublog.backend.domain.trouble.service.factory.PostFactory;
import troublog.backend.domain.trouble.service.query.PostTagQueryService;
import troublog.backend.domain.trouble.service.query.TagQueryService;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.service.query.UserQueryService;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRelationFacade {

	private final ProjectQueryService projectQueryService;
	private final UserQueryService userQueryService;
	private final PostTagQueryService postTagQueryService;
	private final PostTagCommandService postTagCommandService;
	private final TagQueryService tagQueryService;
	private final PostFactory postFactory;
	private final ContentCommandService contentCommandService;

	public void establishRequireRelations(Post createdPost, Long userId, PostCreateReqDto requestDto) {
		setUserRelations(createdPost, userId);
		setProjectRelations(createdPost, requestDto.projectId());
		setErrorTagRelations(createdPost, requestDto.errorTagName());
	}

	public void establishSecondaryRelations(Post post, PostCreateReqDto reqDto) {
		setContentRelations(post, reqDto.contentDtoList());
		setTechStackTagRelations(post, reqDto.postTags());
		//TODO Image URL(String) -> PostImage 변환후 연관관계 메서드 호출 필요
	}

	public void setProjectRelations(Post post, Long projectId) {
		Project project = projectQueryService.findById(projectId);
		project.addPost(post);
	}

	public void setUserRelations(Post post, Long userId) {
		User user = userQueryService.findUserById(userId);
		user.addPost(post);
	}

	public void setErrorTagRelations(Post post, String tagName) {
		PostTag postTag = saveErrorPostTag(tagName, post);
		post.addPostTag(postTag);
	}

	public void setContentRelations(Post post, List<ContentDto> contentDtoList) {
		if (PostFactory.hasContents(contentDtoList)) {
			List<Content> contents = saveAllContent(contentDtoList);
			contents.forEach(post::addContent);
		}
	}

	private PostTag saveErrorPostTag(String tagName, Post post) {
		Tag errorTag = tagQueryService.findErrorTagByName(tagName);
		PostTag postTag = postFactory.createPostTag(errorTag, post);
		return postTagCommandService.save(postTag);
	}

	public List<Content> saveAllContent(List<ContentDto> contentDtoList) {
		List<Content> newContentList = contentDtoList.stream()
			.map(ContentConverter::toEntity)
			.toList();
		return contentCommandService.saveAll(newContentList);
	}

	public void updateRelationsIfChanged(PostUpdateReqDto reqDto, Post foundPost) {
		updateCommonInfo(reqDto, foundPost);
		updateProjectRelations(foundPost, reqDto.projectId());
		updateContentRelations(foundPost, reqDto.contentDtoList());
		updatePostTagRelations(foundPost, reqDto.errorTagName(), reqDto.postTags());
		//TODO Image URL(String) -> PostImage 변환후 연관관계 메서드 호출 필요
	}

	private void setTechStackTagRelations(Post post, List<String> reqTechStackTags) {
		if (PostFactory.hasTechStackTag(reqTechStackTags)) {
			List<PostTag> techStackPostTags = saveTechStackPostTags(reqTechStackTags, post);
			techStackPostTags.forEach(post::addPostTag);
		}
	}

	public List<PostTag> saveTechStackPostTags(List<String> tagNames, Post post) {
		List<Tag> techStackTags = tagQueryService.findTechStackTagsByNames(tagNames);
		List<PostTag> postTags = techStackTags.stream()
			.map(tag -> postFactory.createPostTag(tag, post))
			.toList();
		return postTagCommandService.saveAll(postTags);
	}

	private void setPostImageRelations(Post post, List<String> postImages) {
		if (PostFactory.hasPostImages(postImages)) {
			//TODO 이미지 저장 로직 구현 필요
			// post.addPostImages(postImages);
		}
	}

	private void updateCommonInfo(PostUpdateReqDto reqDto, Post foundPost) {
		foundPost.updateTitle(reqDto.title());
		foundPost.updateIntroduction(reqDto.introduction());
		foundPost.updateVisibility(reqDto.isVisible());
		foundPost.updateStatus(PostStatus.from(reqDto.postStatus()));
		foundPost.updateStarRating(StarRating.from(reqDto.starRating()));
	}

	private void updateProjectRelations(Post post, Long newProjectId) {
		if (post.getProject().getId().equals(newProjectId)) {
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
		deleteAllTagByPostId(post.getId());
		setErrorTagRelations(post, errorTagName);
		setTechStackTagRelations(post, techStackTags);
	}

	public void deleteAllTagByPostId(Long postId) {
		List<PostTag> postTags = postTagQueryService.findAllByPostId(postId);
		postTagCommandService.deleteAll(postTags);
	}

	private void updatePostImageRelations(Post post, List<String> postImages) {
		post.getPostImages().clear();
		setPostImageRelations(post, postImages);
	}
}