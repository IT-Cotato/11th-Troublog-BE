package troublog.backend.global.common.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.common.entity.AuthCode;
import troublog.backend.domain.common.entity.Email;
import troublog.backend.domain.common.repository.AuthCodeRepository;
import troublog.backend.domain.common.repository.EmailRepository;
import troublog.backend.global.common.event.EmailSendEvent;


@Service
@RequiredArgsConstructor
public class MailUtil {

	private static final int AUTH_CODE_LENGTH = 6;
	private static final String PASSWORD_MAIL_SUBJECT = "[Troublog] 비밀번호 재설정 인증코드";
	private static final String PASSWORD_TEMPLATE_PATH = "templates/email/password.html";
	private static final String REPORT_MAIL_SUBJECT = "[Troublog] 신고 접수";
	private static final String REPORT_TEMPLATE_PATH = "templates/email/report.html";
	private static final DateTimeFormatter REPORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private final AuthCodeRepository authCodeRepository;
	private final EmailRepository emailRepository;
	private final ApplicationEventPublisher eventPublisher;

	@Value("${spring.mail.username}")
	private String troubleBlogSenderEmail;

	private final Map<String, String> templateCache = new ConcurrentHashMap<>();

	@Transactional(rollbackFor = Exception.class)
	public UUID sendPasswordMail(String receiverEmailAdr) {

		String randomNumber = generateRandomNumber();
		String emailBody = buildEmailBody(randomNumber);

		Email email = Email.builder()
			.emailTitle(PASSWORD_MAIL_SUBJECT)
			.emailBody(emailBody)
			.senderEmailAdr(troubleBlogSenderEmail)
			.rcvrEmailAdr(receiverEmailAdr)
			.build();

		UUID randomUuid = UUID.randomUUID();

		AuthCode authCode = AuthCode.builder()
			.authCode(randomNumber)
			.expireDate(LocalDateTime.now().plusMinutes(5))
			.isAuth(false)
			.randomString(randomUuid)
			.email(email)
			.build();

		emailRepository.save(email);
		authCodeRepository.save(authCode);
		publishEmailSendEvent(email);

		return randomUuid;
	}

	@Transactional(rollbackFor = Exception.class)
	public Email sendReportMail(
		long reportingUserId,
		long reportedUserId,
		String targetTypeDescription,
		long targetId,
		String reportTypeDescription
	) {
		String emailBody = buildReportEmailBody(
			reportingUserId,
			reportedUserId,
			targetTypeDescription,
			targetId,
			reportTypeDescription,
			LocalDateTime.now()
		);

		Email email = Email.builder()
			.emailTitle(REPORT_MAIL_SUBJECT)
			.emailBody(emailBody)
			.senderEmailAdr(troubleBlogSenderEmail)
			.rcvrEmailAdr(troubleBlogSenderEmail)
			.build();

		Email savedEmail = emailRepository.save(email);

		publishEmailSendEvent(savedEmail);
		return savedEmail;
	}

	private void publishEmailSendEvent(Email email) {
		eventPublisher.publishEvent(new EmailSendEvent(email.getEmailId()));
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
			throw new MailParseException("메일 템플릿 로딩에 실패했습니다: " + path, e);
		}
	}

	private String buildEmailBody(String authCode) {
		// 템플릿 읽기
		String template = getTemplate(PASSWORD_TEMPLATE_PATH);

		// 코드 박스 생성
		String boxesHtml = convertAuthCodeToBoxes(authCode);

		// 템플릿 치환
		return template.replace("{{code-boxes}}", boxesHtml);
	}

	private String buildReportEmailBody(
		long reportingUserId,
		long reportedUserId,
		String targetTypeDescription,
		long targetId,
		String reportTypeDescription,
		LocalDateTime reportedAt
	) {
		String template = getTemplate(REPORT_TEMPLATE_PATH);
		String reportedAtText = reportedAt.format(REPORT_DATE_FORMATTER);

		return template
			.replace("{{reportingUserId}}", String.valueOf(reportingUserId))
			.replace("{{reportedUserId}}", String.valueOf(reportedUserId))
			.replace("{{targetType}}", targetTypeDescription)
			.replace("{{targetId}}", String.valueOf(targetId))
			.replace("{{reportType}}", reportTypeDescription)
			.replace("{{reportedAt}}", reportedAtText);
	}

	private String getTemplate(String path) {
		return templateCache.computeIfAbsent(path, this::loadHtmlTemplate);
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
