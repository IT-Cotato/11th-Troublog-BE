# 🔍 Troublog 모니터링 시스템

## 📋 개요
Grafana + Prometheus + Loki를 활용한 완전한 모니터링 스택을 구축했습니다.

## 🏗️ 아키텍처
```
Spring Boot App ──► Prometheus ──► Grafana
     │                              ▲
     └─── logs ──► Promtail ──► Loki ──┘
```

## 🚀 실행 방법

### 1. 모니터링 스택 시작
```bash
# 모니터링 컨테이너 실행
docker-compose -f docker-compose.monitoring.yml up -d

# 로그 확인
docker-compose -f docker-compose.monitoring.yml logs -f
```

### 2. Spring Boot 앱 실행
```bash
# 의존성 추가 후 빌드
./gradlew clean build

# 앱 실행 (RDS 연결 설정 필요)
./gradlew bootRun
```

## 🌐 접속 정보

| 서비스 | URL | 계정 |
|--------|-----|------|
| **Grafana** | http://localhost:3000 | admin / admin123 |
| **Prometheus** | http://localhost:9090 | - |
| **Loki** | http://localhost:3100 | - |
| **Spring Boot** | http://localhost:8080/actuator/prometheus | - |

## 📊 모니터링 대상

### 시스템 메트릭
- JVM 메모리, GC, 스레드
- HTTP 요청/응답 통계
- 데이터베이스 커넥션 풀

### 애플리케이션 메트릭
- API 엔드포인트 성능
- 에러율 및 응답시간
- 비즈니스 로직 실행 시간

### 로그 모니터링
- 애플리케이션 로그 (JSON 형태)
- 에러 로그 추적
- 사용자 활동 로그

## 🔧 주요 엔드포인트

```bash
# 헬스체크
curl http://localhost:8080/actuator/health

# Prometheus 메트릭
curl http://localhost:8080/actuator/prometheus

# 애플리케이션 정보
curl http://localhost:8080/actuator/info
```

## 📈 Grafana 대시보드
기본 대시보드에서 다음 항목들을 모니터링할 수 있습니다:
- JVM 메모리 사용량
- HTTP 요청 비율
- 응답시간 분포 (50th, 95th percentile)
- 활성 데이터베이스 연결 수
- 실시간 애플리케이션 로그

## 🛑 종료 방법
```bash
# 모니터링 스택 종료
docker-compose -f docker-compose.monitoring.yml down

# 볼륨까지 제거
docker-compose -f docker-compose.monitoring.yml down -v
```

## 🔍 트러블슈팅

### Spring Boot 메트릭이 수집되지 않는 경우
1. `application.yml`의 actuator 설정 확인
2. Prometheus 설정에서 타겟 주소 확인 (`host.docker.internal:8080`)
3. 방화벽 설정 확인

### 로그가 수집되지 않는 경우
1. `logs/` 디렉토리 생성 확인
2. Spring Boot 로그 파일 생성 확인
3. Promtail 컨테이너 볼륨 마운트 확인

### Grafana 대시보드가 로드되지 않는 경우
1. 데이터소스 연결 상태 확인
2. Prometheus/Loki 서비스 상태 확인
3. 브라우저 캐시 클리어