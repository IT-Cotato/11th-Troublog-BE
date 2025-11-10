package troublog.backend.domain.trouble.converter;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.entity.Tag;
import troublog.backend.domain.trouble.enums.TagType;

@UtilityClass
public class TagConverter {

    public Tag toEntity(final String tagName, final String normalizedName) {
        return Tag.builder()
                .name(tagName)
                .normalizedName(normalizedName)
                .tagType(TagType.TECH_STACK)
                .build();
    }

    public PostTag toPostTagEntity(final String displayName) {
        return PostTag.builder()
                .displayName(displayName)
                .build();
    }
}
