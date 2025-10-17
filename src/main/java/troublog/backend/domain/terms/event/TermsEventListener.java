package troublog.backend.domain.terms.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.auth.event.UserRegisteredEvent;
import troublog.backend.domain.terms.facade.command.TermsCommandFacade;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TermsEventListener {
	private final TermsCommandFacade facade;

	@Async
	@TransactionalEventListener
	public void handleUserRegistered(UserRegisteredEvent event) {
		log.info("사용자 회원가입 이벤트 감지 User ID : {}", event.userId());
		facade.agreeToTerms(event.termsAgreements(), event.userId());
	}
}
