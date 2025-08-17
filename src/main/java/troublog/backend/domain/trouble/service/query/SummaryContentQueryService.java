package troublog.backend.domain.trouble.service.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.SummaryContent;
import troublog.backend.domain.trouble.repository.SummaryContentRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SummaryContentQueryService {
	private final SummaryContentRepository summaryContentRepository;

	public List<SummaryContent> findAllByPostSummaryId(Long summaryId) {
		return summaryContentRepository.findAllByPostSummary_Id(summaryId);
	}
}
