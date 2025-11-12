package troublog.backend.domain.alert.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.alert.dto.response.AlertResDto;
import troublog.backend.domain.alert.service.AlertFacade;
import troublog.backend.global.common.annotation.Authentication;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alert")
@Tag(name = "알림", description = "알림 관련 API")
public class AlertController {

	private final AlertFacade alertFacade;

	@GetMapping("/list")
	@Operation(summary = "알림 조회 API", description = """
		사용자 알림 조회 -\s
		comments, likes, troubles 중 하나를 파라미터로 입력,\s
		아무것도 입력 안할 시 전체 알림 list 리턴""")
	@ApiResponse(responseCode = "200", description = "성공",
		content = @Content(array = @ArraySchema(schema = @Schema(implementation = AlertResDto.class))))
	public ResponseEntity<BaseResponse<List<AlertResDto>>> getAlerts(
		@Authentication CustomAuthenticationToken auth,
		@RequestParam(required = false) String alertType) {

		return ResponseUtils.ok(alertFacade.getAlerts(auth.getUserId(), alertType));
	}

	@DeleteMapping
	@Operation(summary = "알림 삭제 API", description = "알림 삭제")
	public ResponseEntity<BaseResponse<Void>> deleteAlert(@RequestParam Long alertId) {

		alertFacade.deleteAlert(alertId);
		return ResponseUtils.noContent();
	}

	@GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@Operation(summary = "SSE 연결 API", description = "실시간 알림을 위한 SSE 연결")
	public SseEmitter connectSse(@Authentication CustomAuthenticationToken auth) {
		Long userId = auth.getUserId();

		return alertFacade.connect(userId);
	}
}
