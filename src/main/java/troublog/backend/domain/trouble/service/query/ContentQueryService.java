package troublog.backend.domain.trouble.service.query;

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
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentQueryService {

	private final ContentRepository contentRepository;

	public List<Content> findContentsWithoutSummaryByPostId(Long postId) {
		return contentRepository.findContentsWithoutSummaryByPostId(postId);
	}
}