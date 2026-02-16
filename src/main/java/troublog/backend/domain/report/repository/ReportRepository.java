package troublog.backend.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.report.entity.Report;
import troublog.backend.domain.report.enums.ReportTargetType;

public interface ReportRepository extends JpaRepository<Report, Long> {
	boolean existsByReportingUser_IdAndTargetTypeAndTargetId(long reportingUserId, ReportTargetType targetType,
		long targetId);
}
