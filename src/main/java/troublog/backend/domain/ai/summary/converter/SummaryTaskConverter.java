package troublog.backend.domain.ai.summary.converter;

import java.time.LocalDateTime;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.ai.summary.entity.SummaryTask;
import troublog.backend.domain.ai.summary.enums.SummaryStatus;
import troublog.backend.global.common.util.IdGenerator;

@UtilityClass
public class SummaryTaskConverter {

	public SummaryTask from(Long postId) {
		return SummaryTask.builder()
			.id(IdGenerator.generate())
			.postId(postId)
			.status(SummaryStatus.PENDING)
			.startedAt(LocalDateTime.now())
			.build();
	}
}