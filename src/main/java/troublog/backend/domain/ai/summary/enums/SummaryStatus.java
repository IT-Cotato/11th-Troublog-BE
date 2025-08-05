package troublog.backend.domain.ai.summary.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SummaryStatus {
	PENDING("요약 작업이 대기열에 등록되었어요! 잠시만 기다려주세요", 0),
	FAILED("요약 작업 중 문제가 발생했어요. 다시 시도해주세요", 0),
	CANCELLED("요약 작업이 취소되었어요. 필요하시면 언제든 다시 시작해주세요", 0),
	STARTED("트러블슈팅 문서 분석을 시작했어요", 10),
	PREPROCESSING("문서 내용을 정리하고 분석 준비를 하고 있어요", 25),
	ANALYZING("AI가 트러블슈팅 문서의 핵심 내용을 꼼꼼히 분석하고 있어요", 50),
	POSTPROCESSING("분석 결과를 정리하고 멋진 요약을 만들고 있어요", 75),
	COMPLETED("요약 완료! 트러블슈팅 문서가 깔끔하게 정리되었어요", 100);

	private final String message;
	private final int progress;
}