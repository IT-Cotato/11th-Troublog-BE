package troublog.backend.domain.report.converter;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.common.entity.Email;
import troublog.backend.domain.report.dto.response.ReportResDto;
import troublog.backend.domain.report.entity.Report;
import troublog.backend.domain.report.enums.ReportTargetType;
import troublog.backend.domain.report.enums.ReportType;
import troublog.backend.domain.user.entity.User;

@UtilityClass
public class ReportConverter {

	public Report toEntity(
		User reportingUser,
		User reportedUser,
		Email email,
		ReportTargetType targetType,
		long targetId,
		ReportType reportType,
		String copyrightImgUrl
	) {
		return Report.builder()
			.reportingUser(reportingUser)
			.reportedUser(reportedUser)
			.email(email)
			.targetType(targetType)
			.targetId(targetId)
			.reportType(reportType)
			.copyrightImgUrl(copyrightImgUrl)
			.build();
	}

	public ReportResDto toResponse(Report report) {
		return ReportResDto.builder()
			.reportId(report.getId())
			.build();
	}
}
