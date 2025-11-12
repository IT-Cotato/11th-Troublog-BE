package troublog.backend.domain.trouble.service.factory;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.converter.PostConverter;
import troublog.backend.domain.trouble.converter.TagConverter;
import troublog.backend.domain.trouble.dto.request.PostReqDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.entity.Tag;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;
import troublog.backend.global.common.util.TagNameFormatter;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostFactory {

    public static void validateAuthorized(final Long requestUserID, final Post post) {
        Long registeredUserID = post.getUser().getId();
        if (!registeredUserID.equals(requestUserID)) {
            throw new PostException(ErrorCode.POST_ACCESS_DENIED);
        }
    }

    public static void validateIsDeleted(final Post foundPost) {
        if (Boolean.FALSE.equals(foundPost.getIsDeleted())) {
            throw new PostException(ErrorCode.POST_NOT_DELETED);
        }
    }

    public Post createPostWithRequireRelations(final PostReqDto postReqDto) {
        PostStatus status = PostStatus.from(postReqDto.postStatus());
        return switch (status) {
            case WRITING -> PostConverter.createWritingPost(postReqDto);
            case COMPLETED -> PostConverter.createCompletedPost(postReqDto);
            case SUMMARIZED -> PostConverter.createSummarizedPost(postReqDto);
        };
    }

    public PostTag createPostTag(final Tag tag, final Post post) {
        PostTag postTag = TagConverter.toPostTagEntity(TagNameFormatter.toDisplayName(tag.getNormalizedName()));
        postTag.assignPost(post);
        postTag.assignTag(tag);
        return postTag;
    }
}