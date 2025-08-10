package troublog.backend.domain.ai.summary.entity;

import java.time.LocalDateTime;

import org.springframework.data.redis.core.RedisHash;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.domain.ai.summary.dto.response.SummarizedResDto;
import troublog.backend.domain.ai.summary.enums.SummaryStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "summaryTask", timeToLive = 3600)
public class SummaryTask {

	@Id
	private String id;

	private Long postId;

	@Builder.Default
	private SummaryStatus status = SummaryStatus.PENDING;

	@Builder.Default
	private Integer progress = 0;

	private SummarizedResDto result;
	private LocalDateTime startedAt;
	private LocalDateTime completedAt;
	private String currentStep;

	public void updateWorkingOnStatus(SummaryStatus status) {
		this.status = status;
		this.progress = status.getProgress();
		this.currentStep = status.getMessage();
		if (status == SummaryStatus.STARTED) {
			this.startedAt = LocalDateTime.now();
		}
	}

	public void updateCompletedStatus(SummaryStatus status) {
		this.status = status;
		this.progress = status.getProgress();
		this.currentStep = status.getMessage();
		this.completedAt = LocalDateTime.now();
	}

	public void registerResult(SummarizedResDto result) {
		this.result = result;
	}

	public boolean isCompleted() {
		return status == SummaryStatus.COMPLETED || status == SummaryStatus.FAILED || status == SummaryStatus.CANCELLED;
	}
}