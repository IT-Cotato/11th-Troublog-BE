package troublog.backend.domain.report.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum ReportType {
	SPAM("스팸 및 홍보성 게시글"),
	INFO("기술적 오류 및 잘못된 정보"),
	BLAME("비방, 욕설 및 혐오 표현"),
	PRIVACY("개인정보노출"),
	SUBJECT("주제와 맞지 않는 게시글"),
	COPYRIGHT("저작권 침해 및 무단 전재");

	private final String description;
}
