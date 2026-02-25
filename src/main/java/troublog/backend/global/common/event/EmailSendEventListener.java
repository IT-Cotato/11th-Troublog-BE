package troublog.backend.global.common.event;

import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.common.entity.Email;
import troublog.backend.domain.common.repository.EmailRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSendEventListener {

	private final EmailRepository emailRepository;
	private final JavaMailSender mailSender;

	@Async("mailExecutor")
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleEmailSendEvent(EmailSendEvent event) {
		emailRepository.findById(event.emailId())
			.ifPresentOrElse(this::sendEmail, () ->
				log.warn("메일 전송 대상 이메일을 찾을 수 없습니다: emailId={}", event.emailId()));
	}

	private void sendEmail(Email email) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(email.getRcvrEmailAdr());
			helper.setFrom(email.getSenderEmailAdr());
			helper.setSubject(email.getEmailTitle());
			helper.setText(email.getEmailBody(), true);

			mailSender.send(message);
			log.info("[Mail] 메일 전송 성공:: mailId={}", email.getEmailId());
		} catch (Exception exception) {
			log.error("메일 전송 실패 {}", email.getEmailId(), exception);
			throw new MailSendException("메일 전송에 실패했습니다: " + email.getEmailId(), exception);
		}
	}
}
