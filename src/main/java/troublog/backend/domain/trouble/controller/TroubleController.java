package troublog.backend.domain.trouble.controller;

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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.dto.response.TroubleResDto;
import troublog.backend.domain.trouble.dto.resquest.TroubleReqDto;
import troublog.backend.domain.trouble.service.command.TroubleCommandService;
import troublog.backend.domain.trouble.service.query.TroubleQueryService;
import troublog.backend.global.common.annotation.Authentication;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequestMapping("/troubles")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "트러블슈팅", description = "트러블슈팅 문서 관련 엔드포인트")
public class TroubleController {

	private final TroubleCommandService commandService;
	private final TroubleQueryService queryService;

	@PostMapping
	@Operation(summary = "트러블슈팅 문서 생성 API", description = "")
	@ApiResponse(responseCode = "200", description = "성공",
		content = @Content(schema = @Schema(implementation = Long.class)))
	public ResponseEntity<BaseResponse<TroubleResDto>> register(
		@Authentication CustomAuthenticationToken token,
		@Valid @RequestBody TroubleReqDto reqDto,
		HttpServletRequest request) {

		TroubleResDto response = commandService.createTroubleDoc();

		return ResponseUtils.created(response);
	}
}