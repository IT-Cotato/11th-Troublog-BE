package troublog.backend.domain.report.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum ReportTargetType {
	POST("게시글"),
	COMMENT("댓글");

	private final String description;
}
