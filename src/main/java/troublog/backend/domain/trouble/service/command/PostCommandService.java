package troublog.backend.domain.trouble.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.converter.PostConverter;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.repository.PostRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCommandService {

	private final PostRepository repository;

	public PostResDto createTroubleDoc() {
		return PostConverter.toResponse();
	}
}
