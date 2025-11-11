package troublog.backend.domain.trouble.service.query;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.Tag;
import troublog.backend.domain.trouble.enums.TagType;
import troublog.backend.domain.trouble.repository.TagRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TagQueryService {

    private final TagRepository tagRepository;

    public Tag findErrorTagByName(final String tagName) {
        log.info("[Tag] 에러 태그 조회: tagName={}", tagName);
        return tagRepository.findTagByNameAndTagType(tagName, TagType.ERROR)
                .orElseThrow(() -> new PostException(ErrorCode.TAG_NOT_FOUND));
    }

    public Optional<Tag> findTechStackTagByNormalizedName(String normalizedName) {
        log.info("[Tag] 기술 스택 태그 조회 (normalizedName): normalizedName={}", normalizedName);
        return tagRepository.findTagByNormalizedNameAndTagType(normalizedName, TagType.TECH_STACK);
    }

    public List<Tag> findTechStackTagContainsName(String keyword) {
        return tagRepository.findTagByNameContaining(keyword);
    }
}