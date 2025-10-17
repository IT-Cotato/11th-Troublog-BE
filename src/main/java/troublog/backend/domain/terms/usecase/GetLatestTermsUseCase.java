package troublog.backend.domain.terms.usecase;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.terms.entity.Terms;
import troublog.backend.domain.terms.service.query.TermsQueryService;
import troublog.backend.domain.terms.validator.TermsValidator;

@Slf4j
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GetLatestTermsUseCase {

	private final TermsQueryService service;

	public List<Terms> execute() {
		List<Terms> currentActiveTerms = service.getCurrentActiveTerms();
			currentActiveTerms.forEach(TermsValidator::validate);
		return currentActiveTerms;
	}
}
