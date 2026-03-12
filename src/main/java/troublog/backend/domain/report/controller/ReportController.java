package troublog.backend.domain.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.report.dto.request.ReportReqDto;
import troublog.backend.domain.report.dto.response.ReportResDto;
import troublog.backend.domain.report.service.facade.ReportCommandFacadeService;
import troublog.backend.global.common.annotation.Authentication;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "신고", description = "신고 관련 API")
public class ReportController {

	private final ReportCommandFacadeService reportCommandFacadeService;

	@PostMapping
	@Operation(summary = "신고 생성 API", description = "게시글/댓글 신고를 접수합니다.")
	@ApiResponse(
		responseCode = "201",
		description = "Created",
		content = @Content(schema = @Schema(implementation = ReportResDto.class))
	)
	public ResponseEntity<BaseResponse<ReportResDto>> createReport(
		@Authentication CustomAuthenticationToken auth,
		@Valid @RequestBody ReportReqDto reportReqDto
	) {
		ReportResDto response = reportCommandFacadeService.createReport(auth.getUserId(), reportReqDto);
		return ResponseUtils.created(response);
	}
}
