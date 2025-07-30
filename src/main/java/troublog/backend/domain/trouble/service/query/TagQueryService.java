package troublog.backend.domain.trouble.service.query;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import troublog.backend.domain.trouble.entity.Tag;
import troublog.backend.domain.trouble.enums.TagType;
import troublog.backend.domain.trouble.repository.TagRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TagQueryService {

	private final TagRepository tagRepository;

	public Tag findErrorTagByName(String tagName) {
		log.info("[Tag] 에러 태그 조회: tagName={}", tagName);
		return tagRepository.findTagByNameAndTagType(tagName, TagType.ERROR)
			.orElseThrow(() -> new PostException(ErrorCode.TAG_NOT_FOUND));
	}

	public List<Tag> findTechStackTagsByNames(List<String> tagNames) {
		List<Tag> tags = tagRepository.findByNameInAndTagType(tagNames, TagType.TECH_STACK);
		log.info("[Tag] 기술 스택 태그 조회 결과: requestedCount={}, foundCount={}", tagNames.size(), tags.size());
		return tags;
	}
}