package troublog.backend.domain.terms.service.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.terms.entity.Terms;
import troublog.backend.domain.terms.repository.TermsRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TermsQueryService {

	private final TermsRepository termsRepository;

	public List<Terms> getCurrentActiveTerms() {
		return termsRepository.findActiveTerms();
	}
}
