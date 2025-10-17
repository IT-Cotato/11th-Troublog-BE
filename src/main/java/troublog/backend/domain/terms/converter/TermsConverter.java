package troublog.backend.domain.terms.converter;

import java.time.LocalDateTime;
import java.util.List;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.terms.dto.response.LatestTermsResDto;
import troublog.backend.domain.terms.dto.response.TermsAgreementResDto;
import troublog.backend.domain.terms.dto.response.common.TermsDto;
import troublog.backend.domain.terms.dto.response.common.UserConsentDto;
import troublog.backend.domain.terms.entity.Terms;
import troublog.backend.domain.terms.entity.UserTermsConsent;
import troublog.backend.domain.user.entity.User;

@UtilityClass
public class TermsConverter {

	public TermsDto toTermsDto(Terms terms) {
		return TermsDto.builder()
			.id(terms.getId())
			.termsType(terms.getTermsType())
			.title(terms.getTitle())
			.body(terms.getBody())
			.isRequired(terms.getIsRequired())
			.expirationPeriod(terms.getExpirationPeriod())
			.build();
	}

	public LatestTermsResDto toLatestTermsResDto(List<Terms> termsList) {
		List<TermsDto> termsDtoList = termsList.stream()
			.map(TermsConverter::toTermsDto)
			.toList();
		return LatestTermsResDto.builder()
			.termsDtoList(termsDtoList)
			.build();
	}

	public UserConsentDto toUserConsentDto(UserTermsConsent consent) {
		return UserConsentDto.builder()
			.userId(consent.getUser().getId())
			.termsId(consent.getTerms().getId())
			.termsType(consent.getTermsType())
			.isAgreed(consent.getIsAgreed())
			.agreedAt(consent.getAgreedAt())
			.expirationAt(consent.getExpirationAt())
			.build();
	}

	public TermsAgreementResDto toTermsAgreementResDto(List<UserTermsConsent> consentList) {
		List<UserConsentDto> userConsentDtoList = consentList.stream()
			.map(TermsConverter::toUserConsentDto)
			.toList();
		return TermsAgreementResDto.builder()
			.userConsentDtos(userConsentDtoList)
			.build();
	}

	public UserTermsConsent toUserTermsConsent(User user, Terms terms, Boolean isAgreed) {
		return UserTermsConsent.builder()
			.user(user)
			.terms(terms)
			.isAgreed(isAgreed)
			.agreedAt(LocalDateTime.now())
			.isCurrent(true)
			.termsType(terms.getTermsType())
			.expirationAt(terms.getExpirationPeriod() != null ?
				LocalDateTime.now().plusYears(terms.getExpirationPeriod()) : null)
			.build();
	}

}
