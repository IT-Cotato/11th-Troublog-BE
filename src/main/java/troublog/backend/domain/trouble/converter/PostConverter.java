package troublog.backend.domain.trouble.converter;

import java.time.LocalDateTime;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.dto.resquest.PostCreateReqDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.enums.StarRating;

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
			.completedAt(LocalDateTime.now())
			.build();
	}

	public PostResDto toResponse(Post post) {
		return PostResDto.builder()
			.id(post.getId())
			.title(post.getTitle())
			.introduction(post.getIntroduction())
			.isVisible(post.isVisible())
			.isSummaryCreated(post.isSummaryCreated())
			.postStatus(post.getStatus().getMessage())
			.starRating(post.getStarRating().name())
			.commentCount(post.getCommentCount())
			.likeCount(post.getLikeCount())
			.completedAt(post.getCompletedAt())
			.build();
	}
}