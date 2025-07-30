package troublog.backend.domain.trouble.service.command;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import troublog.backend.domain.trouble.entity.Content;
import troublog.backend.domain.trouble.repository.ContentRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentCommandService {

	private final ContentRepository contentRepository;

	public List<Content> saveAll(List<Content> contents) {
		log.info("[Content] 트러블슈팅 문서 콘텐츠 일괄 저장: contentSize={}", contents.size());
		return contentRepository.saveAll(contents);
	}
}
