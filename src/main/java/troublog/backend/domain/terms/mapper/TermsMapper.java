package troublog.backend.domain.terms.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import troublog.backend.domain.terms.dto.response.LatestTermsResDto;
import troublog.backend.domain.terms.dto.response.TermsAgreementResDto;
import troublog.backend.domain.terms.dto.response.common.TermsDto;
import troublog.backend.domain.terms.entity.Terms;
import troublog.backend.domain.terms.entity.UserTermsConsent;
import troublog.backend.domain.user.entity.User;

@Mapper(componentModel = "spring")
public interface TermsMapper {
	TermsMapper INSTANCE = Mappers.getMapper(TermsMapper.class);

	TermsDto toTermsDto(Terms terms);

	LatestTermsResDto toLatestTermsResDto(List<Terms> terms);

	TermsAgreementResDto toTermsAgreementResDto(List<UserTermsConsent> result);



	default UserTermsConsent toUserTermsConsent(
		User user,
		Terms terms,
		Boolean isAgreed
	) {
		return UserTermsConsent.builder()
			.user(user)
			.terms(terms)
			.isAgreed(isAgreed)
			.agreedAt(java.time.LocalDateTime.now())
			.isCurrent(true)
			.termsType(terms.getTermsType())
			.build();
	}
}
