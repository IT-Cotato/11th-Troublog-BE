package troublog.backend.domain.auth.event;

import java.util.Map;

import lombok.Builder;

@Builder
public record UserRegisteredEvent(
	Long userId,
	Map<Long, Boolean> termsAgreements
) {
}