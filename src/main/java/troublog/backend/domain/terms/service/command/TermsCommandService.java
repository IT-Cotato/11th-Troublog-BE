package troublog.backend.domain.terms.service.command;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.terms.entity.Terms;
import troublog.backend.domain.terms.repository.TermsRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TermsCommandService {

	private final TermsRepository termsRepository;

	public Terms saveTerms(Terms terms) {
		return termsRepository.save(terms);
	}

}
