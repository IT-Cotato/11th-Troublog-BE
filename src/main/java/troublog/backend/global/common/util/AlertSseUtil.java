package troublog.backend.global.common.util;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.alert.dto.response.AlertResDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertSseUtil {

	// 사용자별 SSE 연결 관리
	private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
	
	private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 1시간
	
	public SseEmitter connect(Long userId) {
		SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
		emitters.put(userId, emitter);
		
		// 연결 종료 시 emitter 제거
		emitter.onCompletion(() -> {
			log.info("[SSE] SSE 연결 완료: userId={}", userId);
			emitters.remove(userId);
		});

		emitter.onTimeout(() -> {
			log.info("[SSE] SSE 연결 타임아웃: userId={}", userId);
			emitters.remove(userId);
		});
		
		emitter.onError((ex) -> {
			log.error("[SSE] SSE 연결 오류: userId={}, error={}", userId, ex.getMessage());
			emitters.remove(userId);
		});
		
		// 연결 확인 메시지 전송
		try {
			emitter.send(SseEmitter.event()
				.name("connect")
				.data("SSE 연결 성공"));
			log.info("[SSE] SSE 연결 성공: userId={}", userId);
			
		} catch (IOException e) {
			log.error("[SSE] 초기 연결 메시지 전송 실패: userId={}", userId, e);
			emitters.remove(userId);
		}
		
		return emitter;
	}
	
	public boolean sendAlert(Long userId, AlertResDto alert) {
		SseEmitter emitter = emitters.get(userId);
		if (emitter != null) {
			try {
				emitter.send(SseEmitter.event()
					.name("alert")
					.data(alert));
				log.info("[SSE] 알림 전송 성공: userId={}, alertType={}", userId, alert.alertType());
				return true;
			} catch (IOException e) {
				log.error("[SSE] 알림 전송 실패: userId={}", userId, e);
				emitters.remove(userId);
				return false;
			}
		} else {
			log.warn("[SSE] SSE 연결이 없음: userId={}", userId);
			return false;
		}
	}
}