package troublog.backend.domain.trouble.service.command;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import troublog.backend.domain.trouble.entity.Tag;
import troublog.backend.domain.trouble.repository.TagRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TagCommandService {
    private final TagRepository tagRepository;

    public Tag save(final Tag tag) {
        log.info("[Tag] 태그 저장: normalizedName={}, tagType={}", tag.getNormalizedName(), tag.getTagType());
        return tagRepository.save(tag);
    }

}
