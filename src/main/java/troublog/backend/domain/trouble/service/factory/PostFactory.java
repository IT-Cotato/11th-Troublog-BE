package troublog.backend.domain.trouble.service.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.converter.PostConverter;
import troublog.backend.domain.trouble.dto.request.ContentDto;
import troublog.backend.domain.trouble.dto.request.PostCreateReqDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.entity.Tag;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostFactory {

	public Post createPostWithRequireRelations(PostCreateReqDto requestDto) {
		PostStatus status = PostStatus.from(requestDto.postStatus());
		return switch (status) {
			case WRITING -> PostConverter.createWritingPost(requestDto);
			case COMPLETED -> PostConverter.createCompletedPost(requestDto);
			case SUMMARIZED -> PostConverter.createSummarizedPost(requestDto);
		};
	}

	public PostTag createPostTag(Tag tag, Post post) {
		PostTag postTag = PostTag.builder().build();
		postTag.assignPost(post);
		postTag.assignTag(tag);
		return postTag;
	}

	public static boolean hasContents(List<ContentDto> contentDtoList) {
		return contentDtoList != null && !contentDtoList.isEmpty();
	}

	public static boolean hasTechStackTag(List<String> postTags) {
		return postTags != null && !postTags.isEmpty();
	}

	public static boolean hasPostImages(List<String> postImages) {

		return postImages != null && !postImages.isEmpty();
	}

	public static void validateAuthorized(Long requestUserID, Post post) {
		Long registeredUserID = post.getUser().getId();
		if (!registeredUserID.equals(requestUserID)) {
			throw new PostException(ErrorCode.POST_ACCESS_DENIED);
		}
	}

	public static void validateIsDeleted(Post foundPost) {
		if (Boolean.FALSE.equals(foundPost.getIsDeleted())) {
			throw new PostException(ErrorCode.POST_NOT_DELETED);
		}
	}
}