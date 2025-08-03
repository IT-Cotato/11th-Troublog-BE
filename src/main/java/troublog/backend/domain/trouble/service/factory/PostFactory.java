package troublog.backend.domain.trouble.service.factory;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.converter.PostConverter;
import troublog.backend.domain.trouble.dto.request.PostReqDto;
import troublog.backend.domain.trouble.dto.request.common.ContentDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.entity.Tag;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostFactory {

	public static boolean hasContents(List<ContentDto> contentDtoList) {
		return contentDtoList != null && !contentDtoList.isEmpty();
	}

	public static boolean hasTechStackTag(List<String> postTags) {
		return postTags != null && !postTags.isEmpty();
	}

	public static boolean hasFiles(List<MultipartFile> postImages) {
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

	public Post createPostWithRequireRelations(PostReqDto postReqDto) {
		PostStatus status = PostStatus.from(postReqDto.postStatus());
		return switch (status) {
			case WRITING -> PostConverter.createWritingPost(postReqDto);
			case COMPLETED -> PostConverter.createCompletedPost(postReqDto);
			case SUMMARIZED -> PostConverter.createSummarizedPost(postReqDto);
		};
	}

	public PostTag createPostTag(Tag tag, Post post) {
		PostTag postTag = PostTag.builder().build();
		postTag.assignPost(post);
		postTag.assignTag(tag);
		return postTag;
	}
}