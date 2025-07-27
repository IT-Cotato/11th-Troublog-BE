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
	private static final int DEFAULT_COUNT = 0;
	private static final boolean DEFAULT_VISIBLE = false;
	private static final boolean DEFAULT_SUMMARY_CREATED = false;

	public Post createWritingPost(PostCreateReqDto requestDto) {
		return createBasePost(requestDto)
			.status(PostStatus.WRITING)
			.isVisible(DEFAULT_VISIBLE)
			.isSummaryCreated(DEFAULT_SUMMARY_CREATED)
			.build();
	}

	public Post createCompletedPost(PostCreateReqDto requestDto) {
		return createBasePost(requestDto)
			.introduction(requestDto.introduction())
			.isVisible(requestDto.isVisible())
			.isSummaryCreated(DEFAULT_SUMMARY_CREATED)
			.status(PostStatus.COMPLETED)
			.starRating(StarRating.from(requestDto.starRating()))
			.completedAt(LocalDateTime.now())
			.build();
	}

	public Post createSummarizedPost(PostCreateReqDto requestDto) {
		//TODO 이후 AI 서비스 개발시 AI 서비스 요청과 함께 전송
		return createBasePost(requestDto)
			.introduction(requestDto.introduction())
			.isVisible(requestDto.isVisible())
			.isSummaryCreated(true)
			.status(PostStatus.SUMMARIZED)
			.starRating(StarRating.from(requestDto.starRating()))
			.completedAt(LocalDateTime.now())
			.build();
	}

	private Post.PostBuilder createBasePost(PostCreateReqDto requestDto) {
		return Post.builder()
			.title(requestDto.title())
			.commentCount(DEFAULT_COUNT)
			.likeCount(DEFAULT_COUNT)
			.isDeleted(false);
	}

	public PostResDto toResponse(Post post) {
		return PostResDto.builder()
			.id(post.getId())
			.title(post.getTitle())
			.introduction(post.getIntroduction())
			.likeCount(post.getLikeCount())
			.commentCount(post.getCommentCount())
			.isVisible(post.getIsVisible())
			.isSummaryCreated(post.getIsSummaryCreated())
			.postStatus(post.getStatus().getMessage())
			.starRating(post.getStarRating() != null ? post.getStarRating().name() : null)
			.createdAt(post.getCreated_at())
			.updatedAt(post.getUpdated_at())
			.userId(post.getUser().getId())
			.projectId(post.getProject().getId())
			.errorTag(findErrorTag(post))
			.postTags(findTechStackTags(post))
			.contents(findContents(post))
			.build();
	}

	public List<PostResDto> toResponseList(List<Post> posts) {
		return posts.stream()
			.map(PostConverter::toResponse)
			.toList();
	}

	/**
	 * Post에서 에러 태그 추출
	 */
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

	/**
	 * Post에서 기술 스택 태그 목록 추출
	 */
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

	/**
	 * Post에서 콘텐츠 정보 목록 추출
	 */
	private List<ContentInfoDto> findContents(Post post) {
		if (post.getContents() == null || post.getContents().isEmpty()) {
			return List.of();
		}
		return post.getContents().stream()
			.map(ContentConverter::toResponse)
			.toList();
	}

}