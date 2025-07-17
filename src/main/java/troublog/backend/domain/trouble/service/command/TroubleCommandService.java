package troublog.backend.domain.trouble.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.converter.TroubleConverter;
import troublog.backend.domain.trouble.dto.response.TroubleResDto;
import troublog.backend.domain.trouble.dto.resquest.TroubleReqDto;
import troublog.backend.domain.trouble.repository.TroubleRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TroubleCommandService {

	private final TroubleRepository repository;

	public TroubleResDto createTroubleDoc() {
		return TroubleConverter.toResponse();
	}
}
