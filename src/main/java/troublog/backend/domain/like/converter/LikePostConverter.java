package troublog.backend.domain.like.converter;

import troublog.backend.domain.like.dto.response.LikePostResDto;

public class LikePostConverter {

    public static LikePostResDto toResponse(Post post) {
        return LikePostResDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .errorTag(post.getErrorTag())
                .postTags(post.getTags().stream()
                        .map(Tag::getName)
                        .collect(Collectors.toList()))
                .contents(post.getContents())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .createdAt(post.getCreatedAt())
                .build();
    }
}