package troublog.backend.domain.trouble.service.query;

import java.util.List;

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

	public Tag findErrorTagsByNames(String tagName) {
		return tagRepository.findByName(tagName)
			.orElseThrow(() -> new PostException(ErrorCode.TAG_NOT_FOUND));
	}

	public List<Tag> findTechStackTagsByNames(List<String> tagNames) {
		return tagRepository.findByNameInAndTagType(tagNames, TagType.TECH_STACK)
			.orElseThrow(() -> new PostException(ErrorCode.TAG_NOT_FOUND));
	}
}