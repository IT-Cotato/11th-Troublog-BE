package troublog.backend.domain.report.service.facade.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.common.entity.Email;
import troublog.backend.domain.report.converter.ReportConverter;
import troublog.backend.domain.report.dto.request.ReportReqDto;
import troublog.backend.domain.report.dto.response.ReportResDto;
import troublog.backend.domain.report.entity.Report;
import troublog.backend.domain.report.enums.ReportTargetType;
import troublog.backend.domain.report.service.command.ReportCommandService;
import troublog.backend.domain.report.service.query.ReportQueryService;
import troublog.backend.domain.report.validator.ReportValidator;
import troublog.backend.domain.trouble.entity.Comment;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.service.query.CommentQueryService;
import troublog.backend.domain.trouble.service.query.PostQueryService;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.service.query.UserQueryService;
import troublog.backend.global.common.util.MailUtil;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportCommandFacade {

	private final ReportCommandService reportCommandService;
	private final ReportQueryService reportQueryService;
	private final UserQueryService userQueryService;
	private final PostQueryService postQueryService;
	private final CommentQueryService commentQueryService;
	private final MailUtil mailUtil;

	/**
	 * 신고 작성 (POST)
	 */
	public ReportResDto createReport(long authUserId, ReportReqDto reportReqDto) {
		ReportValidator.validateReporterMatches(authUserId, reportReqDto.reportingUserId());

		boolean alreadyReported = reportQueryService.existsByReporterAndTarget(
			reportReqDto.reportingUserId(),
			reportReqDto.targetType(),
			reportReqDto.targetId()
		);
		ReportValidator.validateDuplicate(alreadyReported);

		User reportingUser = userQueryService.findUserById(reportReqDto.reportingUserId());
		User reportedUser = resolveReportedUser(reportReqDto.targetType(), reportReqDto.targetId(),
			reportReqDto.reportedUserId());

		Email email = mailUtil.sendReportMail(
			reportReqDto.reportingUserId(),
			reportReqDto.reportedUserId(),
			reportReqDto.targetType().getDescription(),
			reportReqDto.targetId(),
			reportReqDto.reportType().getDescription()
		);

		Report report = ReportConverter.toEntity(
			reportingUser,
			reportedUser,
			email,
			reportReqDto.targetType(),
			reportReqDto.targetId(),
			reportReqDto.reportType(),
			reportReqDto.copyRightImgUrl()
		);

		Report savedReport = reportCommandService.save(report);
		return ReportConverter.toResponse(savedReport);
	}

	private User resolveReportedUser(ReportTargetType targetType, long targetId, long reportedUserId) {
		if (targetType == ReportTargetType.POST) {
			Post post = postQueryService.findById(targetId);
			ReportValidator.validateReportedUserMatchesTarget(reportedUserId, post.getUser().getId());
			return post.getUser();
		}

		Comment comment = commentQueryService.findComment(targetId);
		ReportValidator.validateReportedUserMatchesTarget(reportedUserId, comment.getUser().getId());
		return comment.getUser();
	}
}
