package troublog.backend.domain.report.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.report.entity.Report;
import troublog.backend.domain.report.repository.ReportRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportCommandService {

	private final ReportRepository reportRepository;

	public Report save(Report report) {
		log.info("[Report] 신고 저장: targetType={}, targetId={}, reportType={}",
			report.getTargetType(), report.getTargetId(), report.getReportType());
		return reportRepository.save(report);
	}
}
