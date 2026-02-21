package troublog.backend.domain.report.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.report.enums.ReportTargetType;
import troublog.backend.domain.report.repository.ReportRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportQueryService {

	private final ReportRepository reportRepository;

	public boolean existsByReporterAndTarget(long reportingUserId, ReportTargetType targetType, long targetId) {
		return reportRepository.existsByReportingUser_IdAndTargetTypeAndTargetId(reportingUserId, targetType, targetId);
	}
}
