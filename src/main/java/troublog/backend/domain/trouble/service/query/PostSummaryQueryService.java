package troublog.backend.domain.trouble.service.query;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.PostSummary;
import troublog.backend.domain.trouble.repository.PostSummaryRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSummaryQueryService {

	private final PostSummaryRepository postSummaryRepository;

	public PostSummary findById(Long id) {
		return postSummaryRepository.findById(id)
			.orElseThrow(() -> new PostException(ErrorCode.POST_SUMMARY_NOT_FOUND));
	}
}
