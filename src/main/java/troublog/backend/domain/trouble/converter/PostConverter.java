package troublog.backend.domain.trouble.converter;

import java.time.LocalDateTime;
import java.util.List;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.dto.request.PostReqDto;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.enums.StarRating;
import troublog.backend.domain.trouble.service.facade.query.PostQueryFacade;

@UtilityClass
public class PostConverter {
	private static final int DEFAULT_COUNT = 0;
	private static final boolean DEFAULT_VISIBLE = false;
	private static final boolean DEFAULT_SUMMARY_CREATED = false;
	private static final boolean DEFAULT_DELETE_STATUS = false;

	public Post createWritingPost(PostReqDto postReqDto) {
		return createBasePost(postReqDto)
			.status(PostStatus.WRITING)
			.isVisible(DEFAULT_VISIBLE)
			.isSummaryCreated(DEFAULT_SUMMARY_CREATED)
			.build();
	}

	public Post createCompletedPost(PostReqDto postReqDto) {
		return createBasePost(postReqDto)
			.introduction(postReqDto.introduction())
			.isVisible(postReqDto.isVisible())
			.isSummaryCreated(DEFAULT_SUMMARY_CREATED)
			.status(PostStatus.COMPLETED)
			.starRating(StarRating.from(postReqDto.starRating()))
			.completedAt(LocalDateTime.now())
			.build();
	}

	public Post createSummarizedPost(PostReqDto postReqDto) {
		//TODO 이후 AI 서비스 개발시 AI 서비스 요청과 함께 전송
		return createBasePost(postReqDto)
			.introduction(postReqDto.introduction())
			.isVisible(postReqDto.isVisible())
			.isSummaryCreated(true)
			.status(PostStatus.SUMMARIZED)
			.starRating(StarRating.from(postReqDto.starRating()))
			.completedAt(LocalDateTime.now())
			.build();
	}

	private Post.PostBuilder createBasePost(PostReqDto postReqDto) {
		return Post.builder()
			.title(postReqDto.title())
			.commentCount(DEFAULT_COUNT)
			.likeCount(DEFAULT_COUNT)
			.isDeleted(DEFAULT_DELETE_STATUS);
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
			.errorTag(PostQueryFacade.findErrorTag(post))
			.postTags(PostQueryFacade.findTechStackTags(post))
			.contents(PostQueryFacade.findContents(post))
			.build();
	}

	public List<PostResDto> toResponseList(List<Post> posts) {
		return posts.stream()
			.map(PostConverter::toResponse)
			.toList();
	}
}