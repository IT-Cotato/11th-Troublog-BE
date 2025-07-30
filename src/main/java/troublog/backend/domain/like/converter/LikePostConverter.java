package troublog.backend.domain.like.converter;

import troublog.backend.domain.image.entity.PostImage;
import troublog.backend.domain.like.dto.response.LikePostResDto;
import troublog.backend.domain.trouble.entity.Content;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.entity.Tag;
import troublog.backend.domain.trouble.enums.TagType;

import java.util.ArrayList;
import java.util.List;

public class LikePostConverter {

    public static LikePostResDto toResponse(Post post) {
        String errorTag = null;
        List<String> techTag = new ArrayList<>();
        List<String> contentBodies = new ArrayList<>();
        List<String> imageList = new ArrayList<>();

        for (PostTag pt : post.getPostTags()) {
            Tag tag = pt.getTag();
            if (tag.getTagType() == TagType.ERROR) {
                errorTag = tag.getName();
            } else if (tag.getTagType() == TagType.TECH_STACK) {
                techTag.add(tag.getName());
            }
        }

        for (Content content : post.getContents()) {
            contentBodies.add(content.getBody());
        }

        for (PostImage postimage : post.getPostImages()) {
            imageList.add(postimage.getImageUrl());
        }

        return LikePostResDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .techTags(techTag)
                .errorTags(errorTag)
                .contents(contentBodies)
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .createdAt(post.getCompletedAt())
                .images(imageList)
                .build();
    }
}