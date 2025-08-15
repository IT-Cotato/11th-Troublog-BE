package troublog.backend.domain.trouble.service.facade.relation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.project.service.query.ProjectQueryService;
import troublog.backend.domain.trouble.converter.ContentConverter;
import troublog.backend.domain.trouble.dto.request.PostReqDto;
import troublog.backend.domain.trouble.dto.request.common.ContentDto;
import troublog.backend.domain.trouble.entity.Content;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.entity.Tag;
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

	public void establishRequireRelations(Post createdPost, Long userId, PostReqDto postReqDto) {
		setUserRelations(createdPost, userId);
		setProjectRelations(createdPost, postReqDto.projectId());
		setErrorTagRelations(createdPost, postReqDto.errorTagName());
	}

	public void establishSecondaryRelations(Post post, PostReqDto postReqDto) {
		setContentRelations(post, postReqDto.contentDtoList());
		setTechStackTagRelations(post, postReqDto.postTags());
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

	public void updateRelationsIfChanged(PostReqDto postReqDto, Post foundPost) {
		updateCommonInfo(postReqDto, foundPost);
		updateProjectRelations(foundPost, postReqDto.projectId());
		updateContentRelations(foundPost, postReqDto.contentDtoList());
		updatePostTagRelations(foundPost, postReqDto.errorTagName(), postReqDto.postTags());
	}

	private void setTechStackTagRelations(Post post, List<String> techStackTags) {
		if (PostFactory.hasTechStackTag(techStackTags)) {
			List<PostTag> techStackPostTags = saveTechStackPostTags(techStackTags, post);
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

	private void updateCommonInfo(PostReqDto postReqDto, Post foundPost) {
		foundPost.updateCommonInfo(postReqDto);
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

	public void setPostSummaryRelation(Post post, PostSummary postSummary) {
		post.addPostSummary(postSummary);
	}
}