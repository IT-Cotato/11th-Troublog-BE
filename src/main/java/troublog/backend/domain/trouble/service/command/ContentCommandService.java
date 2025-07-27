package troublog.backend.domain.trouble.service.command;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.Content;
import troublog.backend.domain.trouble.repository.ContentRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentCommandService {

	private final ContentRepository contentRepository;

	public List<Content> saveAll(List<Content> contents) {
		return contentRepository.saveAll(contents);
	}
}
