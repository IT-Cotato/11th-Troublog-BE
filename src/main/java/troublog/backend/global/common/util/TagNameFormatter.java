package troublog.backend.global.common.util;

import com.google.common.base.CaseFormat;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class TagNameFormatter {

    public static final String OPERATOR = "-";
    public static final String SPACE = " ";
    public static final String WHITESPACE_CHARACTERS = "\\s+";

    public List<String> toDisplayNames(final List<String> normalizedNames) {
        if (CollectionUtils.isEmpty(normalizedNames)) {
            return normalizedNames;
        }

        return normalizedNames.stream()
                .map(TagNameFormatter::toDisplayName)
                .toList();
    }

    private String toDisplayName(final String normalizedName) {
        if (!StringUtils.hasText(normalizedName)) {
            return normalizedName;
        }

        return Arrays.stream(normalizedName.replace(OPERATOR, SPACE).split(WHITESPACE_CHARACTERS))
                .filter(StringUtils::hasText)
                .map(String::toLowerCase)
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(SPACE));
    }

    public List<String> toNormalizedNames(final List<String> displayNames) {
        if (CollectionUtils.isEmpty(displayNames)) {
            return displayNames;
        }

        return displayNames.stream()
                .map(TagNameFormatter::toNormalizedName)
                .toList();
    }

    private String toNormalizedName(final String displayName) {
        if (!StringUtils.hasText(displayName)) {
            return displayName;
        }

        return displayName
                .trim()
                .toLowerCase()
                .replaceAll(WHITESPACE_CHARACTERS, OPERATOR);
    }

    public String toCamelCaseName(final String normalizedName) {
        return CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, normalizedName);
    }
}