package troublog.backend.domain.terms.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import troublog.backend.domain.terms.dto.response.common.TermsDto;

@Schema(description = "최신 약관 정보 응답")
public record LatestTermsResDto(
	@Schema(description = "약관 목록 (서비스 이용약관, 개인정보처리방침 등)")
	List<TermsDto> termsDtoList
) {
}
