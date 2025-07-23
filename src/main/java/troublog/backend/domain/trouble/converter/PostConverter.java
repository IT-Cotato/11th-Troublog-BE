package troublog.backend.domain.trouble.converter;

import java.time.LocalDateTime;
import java.util.List;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.dto.request.PostCreateReqDto;
import troublog.backend.domain.trouble.dto.response.ContentInfoDto;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.entity.Tag;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.enums.StarRating;
import troublog.backend.domain.trouble.enums.TagType;

@UtilityClass
public class PostConverter {

	public Post toEntity(PostCreateReqDto request) {
		return Post.builder()
			.title(request.title())
			.introduction(request.introduction())
			.isVisible(request.isVisible())
			.isSummaryCreated(request.isSummaryCreated())
			.status(PostStatus.from(request.postStatus()))
			.starRating(StarRating.from(request.starRating()))
			.commentCount(0)
			.likeCount(0)
			.completedAt(PostStatus.from(request.postStatus()) == PostStatus.COMPLETED ? LocalDateTime.now() : null)
			.build();
	}

	public PostResDto toResponse(Post post) {
		return PostResDto.builder()
			.id(post.getId())
			.title(post.getTitle())
			.introduction(post.getIntroduction())
			.likeCount(post.getLikeCount())
			.commentCount(post.getCommentCount())
			.isVisible(post.isVisible())
			.isSummaryCreated(post.isSummaryCreated())
			.postStatus(post.getStatus().getMessage())
			.starRating(post.getStarRating().name())
			.createdAt(post.getCreated_at())
			.updatedAt(post.getUpdated_at())
			.userId(post.getUser().getId())
			.projectId(post.getProject().getId())
			.errorTag(findErrorTag(post))
			.postTags(findTechStackTags(post))
			.contents(findContents(post))
			.build();
	}

	private String findErrorTag(Post post) {
		if (post.getPostTags() == null || post.getPostTags().isEmpty()) {
			return null;
		}
		return post.getPostTags().stream()
			.map(PostTag::getTag)
			.filter(tag -> tag != null && tag.getTagType() == TagType.ERROR)
			.map(Tag::getName)
			.findFirst()
			.orElse(null);
	}

	private List<String> findTechStackTags(Post post) {
		if (post.getPostTags() == null || post.getPostTags().isEmpty()) {
			return List.of();
		}
		return post.getPostTags().stream()
			.map(PostTag::getTag)
			.filter(tag -> tag != null && tag.getTagType() == TagType.TECH_STACK)
			.map(Tag::getName)
			.toList();
	}

	private List<ContentInfoDto> findContents(Post post) {
		if (post.getContents() == null || post.getContents().isEmpty()) {
			return List.of();
		}
		return post.getContents().stream()
			.map(ContentConverter::toResponse)
			.toList();
	}

}