package troublog.backend.domain.trouble.service.command;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.converter.ContentConverter;
import troublog.backend.domain.trouble.dto.resquest.ContentDto;
import troublog.backend.domain.trouble.entity.Content;
import troublog.backend.domain.trouble.repository.ContentRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentCommandService {

	private final ContentRepository contentRepository;

	public List<Content> saveAllContent(List<ContentDto> contentDtoList) {
		List<Content> newContentList = contentDtoList.stream()
			.map(ContentConverter::toEntity)
			.toList();
		return contentRepository.saveAll(newContentList);
	}
}
