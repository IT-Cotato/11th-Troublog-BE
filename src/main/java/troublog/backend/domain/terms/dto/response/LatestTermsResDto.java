package troublog.backend.domain.terms.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import troublog.backend.domain.terms.dto.response.common.TermsDto;

@Builder
@Schema(description = "최신 약관 정보 응답 DTO - 회원가입 시 사용자에게 제공할 현재 시점의 약관 목록")
public record LatestTermsResDto(
	@Schema(
		description = "현재 활성 상태인 약관 목록 (서비스 이용약관, 개인정보처리방침) " +
			"회원가입 화면에서 사용자에게 표시할 약관 정보입니다."
	)
	List<TermsDto> termsDtoList
) {
}
