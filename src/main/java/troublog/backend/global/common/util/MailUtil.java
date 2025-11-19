package troublog.backend.global.common.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.common.entity.AuthCode;
import troublog.backend.domain.common.entity.Email;
import troublog.backend.domain.common.repository.AuthCodeRepository;
import troublog.backend.domain.common.repository.EmailRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.UserException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailUtil {

	private static final int AUTH_CODE_LENGTH = 6;
	private static final String MAIL_SUBJECT = "[Troublog] 비밀번호 재설정 인증코드";

	private final AuthCodeRepository authCodeRepository;
	private final EmailRepository emailRepository;
	private final JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String troubleBlogSenderEmail;

	@Transactional(rollbackFor = Exception.class)
	public UUID sendMail(String receiverEmailAdr) {

		String randomNumber = generateRandomNumber();
		String emailBody = buildEmailBody(randomNumber);

		Email email = Email.builder()
			.emailTitle(MAIL_SUBJECT)
			.emailBody(emailBody)
			.senderEmailAdr(troubleBlogSenderEmail)
			.rcvrEmailAdr(receiverEmailAdr)
			.build();

		UUID randomUUID = UUID.randomUUID();

		AuthCode authCode = AuthCode.builder()
			.authCode(randomNumber)
			.expireDate(LocalDateTime.now().plusMinutes(5))
			.isAuth(false)
			.randomString(randomUUID)
			.build();

		emailRepository.save(email);
		authCodeRepository.save(authCode);

		sendEmail(receiverEmailAdr, emailBody);

		return randomUUID;
	}

	private void sendEmail(String receiverEmailAdr, String emailBody) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(receiverEmailAdr);
			helper.setFrom(troubleBlogSenderEmail);
			helper.setSubject(MAIL_SUBJECT);
			helper.setText(emailBody, true);

			mailSender.send(message);
		} catch (Exception exception) {
			log.error("메일 전송 실패 {}", receiverEmailAdr, exception);
			throw new UserException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	private String generateRandomNumber() {
		int randomNumber = ThreadLocalRandom.current().nextInt((int)Math.pow(10, AUTH_CODE_LENGTH - 1),
			(int)Math.pow(10, AUTH_CODE_LENGTH));
		return String.format("%0" + AUTH_CODE_LENGTH + "d", randomNumber);
	}

	private String loadHtmlTemplate(String path) {
		try {
			ClassPathResource resource = new ClassPathResource(path);
			return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("템플릿 로딩 실패: " + path, e);
		}
	}

	private String buildEmailBody(String authCode) {
		// 템플릿 읽기
		String template = loadHtmlTemplate("templates/email/password.html");

		// 코드 박스 생성
		String boxesHtml = convertAuthCodeToBoxes(authCode);

		// 템플릿 치환
		return template.replace("{{code-boxes}}", boxesHtml);
	}

	private String convertAuthCodeToBoxes(String authCode) {
		StringBuilder sb = new StringBuilder();

		for (char c : authCode.toCharArray()) {
			sb.append("""
            <span style="display:inline-block;
                         width:50px; height:60px;
                         line-height:60px; /* 세로 가운데 정렬 */
                         background-color:#f5edff;
                         border-radius:10px;
                         margin:0 6px;
                         font-size:28px;
                         font-weight:700;
                         color:#000000;
                         box-shadow:0 2px 6px rgba(0,0,0,0.05);">
                %s
            </span>
        """.formatted(c));
		}

		return sb.toString();
	}
}
