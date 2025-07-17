package troublog.backend.domain.trouble.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.entity.Trouble;
import troublog.backend.domain.trouble.repository.TroubleRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.TroubleException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TroubleQueryService {
	private final TroubleRepository troubleRepository;

	public Trouble findTroubleById(long id) {
		return troubleRepository.findById(id)
			.orElseThrow(() -> new TroubleException(ErrorCode.TROUBLE_NOT_FOUND));
	}
}
