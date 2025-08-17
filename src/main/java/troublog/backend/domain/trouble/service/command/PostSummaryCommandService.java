package troublog.backend.domain.trouble.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.repository.PostSummaryRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSummaryCommandService {

	private final PostSummaryRepository postSummaryRepository;

	public PostSummary save(PostSummary postSummary) {
		return postSummaryRepository.save(postSummary);
	}
}
