package troublog.backend.domain.trouble.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.ErrorTag;
import troublog.backend.domain.trouble.repository.ErrorTagRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorTagQueryService {
	private final ErrorTagRepository errorTagRepository;

	public ErrorTag findErrorTagById(long id) {
		return errorTagRepository.findById(id)
			.orElseThrow(() -> new PostException(ErrorCode.INVALID_VALUE));
	}
}