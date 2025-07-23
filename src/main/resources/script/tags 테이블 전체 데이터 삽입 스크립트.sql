-- ============================================
-- tags 테이블 전체 데이터 삽입 스크립트
-- 구조: tag_id(AUTO_INCREMENT), created_at, updated_at, description, name, tag_type, tag_category
-- 작성일: 2025-07-22
-- ============================================

-- 테이블 초기화 (선택사항 - 기존 데이터 삭제 시 주석 해제)
-- DELETE FROM tags;
-- ALTER TABLE tags AUTO_INCREMENT = 1;

-- ============================================
-- 🚨 ERROR 태그 삽입
-- ============================================

INSERT INTO tags (name, tag_type, tag_category, created_at, updated_at, description)
VALUES ('Build/Compile Error', 'ERROR', 'BUILD', NOW(), NOW(), NULL),
       ('Runtime Error', 'ERROR', 'RUNTIME', NOW(), NOW(), NULL),
       ('Dependency/Version Error', 'ERROR', 'DEPENDENCY', NOW(), NOW(), NULL),
       ('Network/API Error', 'ERROR', 'NETWORK', NOW(), NOW(), NULL),
       ('Authentication/Authorization Error', 'ERROR', 'AUTH', NOW(), NOW(), NULL),
       ('Database Error', 'ERROR', 'DATABASE_ERROR', NOW(), NOW(), NULL),
       ('UI/Rendering Error', 'ERROR', 'UI', NOW(), NOW(), NULL),
       ('Configuration Error', 'ERROR', 'CONFIG', NOW(), NOW(), NULL),
       ('Timeout/Error Handling', 'ERROR', 'TIMEOUT', NOW(), NOW(), NULL),
       ('Third-party Library Error', 'ERROR', 'LIBRARY', NOW(), NOW(), NULL);

-- ============================================
-- 🎨 FRONTEND 기술 스택 태그 삽입
-- ============================================

INSERT INTO tags (name, tag_type, tag_category, created_at, updated_at, description)
VALUES
-- 기본 웹 기술a
('HTML', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('CSS', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('JavaScript', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('TypeScript', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),

-- 프레임워크/라이브러리
('React', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('Vue.js', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('Angular', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('Next.js', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),

-- 상태관리
('Redux', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('Zustand', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),

-- CSS 프레임워크/라이브러리
('Tailwind CSS', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('Bootstrap', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('Material-UI', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),

-- 기타 프론트엔드 도구 및 개념
('jQuery', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('Axios', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('Fetch', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('Vite', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('Webpack', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('ESLint', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('npm', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('yarn', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('Error Boundary', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('Props', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('Component', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL),
('HTTP', 'TECH_STACK', 'FRONTEND', NOW(), NOW(), NULL);

-- ============================================
-- ⚙️ BACKEND 기술 스택 태그 삽입
-- ============================================

INSERT INTO tags (name, tag_type, tag_category, created_at, updated_at, description)
VALUES
-- 프로그래밍 언어
('Java', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('Kotlin', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('Python', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('Node.js', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('Go', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('Rust', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('Scala', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('Ruby', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('PHP', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('C#', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),

-- 프레임워크
('ASP.NET Core', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('Spring Boot', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('Django', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('Flask', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('Express.js', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('NestJS', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('Laravel', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('Ruby on Rails', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),

-- API 및 통신 기술
('REST API', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('GraphQL', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('WebSocket', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('SSE', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),

-- 인증/보안
('Session', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('JWT', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('OAuth', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL),
('Helmet', 'TECH_STACK', 'BACKEND', NOW(), NOW(), NULL);

-- ============================================
-- 🗄️ DATABASE 기술 스택 태그 삽입
-- ============================================

INSERT INTO tags (name, tag_type, tag_category, created_at, updated_at, description)
VALUES
-- 쿼리 언어
('SQL', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),
('NoSQL', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),

-- 데이터베이스 종류
('MySQL', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),
('PostgreSQL', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),
('MongoDB', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),
('Redis', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),
('MariaDB', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),

-- 데이터베이스 설계 및 개념
('데이터 정규화', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),
('ERD', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),
('트랜잭션', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),
('Key', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),
('Index', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),
('JOIN', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),
('스키마', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),
('ORM', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),

-- 성능 최적화
('Lock', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),
('Slow Query', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),
('Connection Timeout', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL),
('Query Optimization', 'TECH_STACK', 'DATABASE', NOW(), NOW(), NULL);

-- ============================================
-- 🛠️ DEVOPS 기술 스택 태그 삽입
-- ============================================

INSERT INTO tags (name, tag_type, tag_category, created_at, updated_at, description)
VALUES
-- 운영체제 및 스크립트
('Linux', 'TECH_STACK', 'DEVOPS', NOW(), NOW(), NULL),
('Bash', 'TECH_STACK', 'DEVOPS', NOW(), NOW(), NULL),

-- 컨테이너 및 오케스트레이션
('Docker', 'TECH_STACK', 'DEVOPS', NOW(), NOW(), NULL),
('Kubernetes', 'TECH_STACK', 'DEVOPS', NOW(), NOW(), NULL),

-- 인프라 코드화 도구
('Terraform', 'TECH_STACK', 'DEVOPS', NOW(), NOW(), NULL),
('Ansible', 'TECH_STACK', 'DEVOPS', NOW(), NOW(), NULL),

-- CI/CD 도구
('Jenkins', 'TECH_STACK', 'DEVOPS', NOW(), NOW(), NULL),
('GitHub Actions', 'TECH_STACK', 'DEVOPS', NOW(), NOW(), NULL),
('ArgoCD', 'TECH_STACK', 'DEVOPS', NOW(), NOW(), NULL),
('Helm', 'TECH_STACK', 'DEVOPS', NOW(), NOW(), NULL),

-- 모니터링 및 로깅
('Prometheus', 'TECH_STACK', 'DEVOPS', NOW(), NOW(), NULL),
('Grafana', 'TECH_STACK', 'DEVOPS', NOW(), NOW(), NULL),

-- DevOps 개념 및 프로세스
('CI/CD 파이프라인', 'TECH_STACK', 'DEVOPS', NOW(), NOW(), NULL),
('모니터링/로깅', 'TECH_STACK', 'DEVOPS', NOW(), NOW(), NULL),
('서버 상태 관리', 'TECH_STACK', 'DEVOPS', NOW(), NOW(), NULL),
('인프라 코드화', 'TECH_STACK', 'DEVOPS', NOW(), NOW(), NULL);

-- ============================================
-- ☁️ INFRA 기술 스택 태그 삽입
-- ============================================

INSERT INTO tags (name, tag_type, tag_category, created_at, updated_at, description)
VALUES
-- 클라우드 서비스
('AWS', 'TECH_STACK', 'INFRA', NOW(), NOW(), NULL),
('GCP', 'TECH_STACK', 'INFRA', NOW(), NOW(), NULL),
('Azure', 'TECH_STACK', 'INFRA', NOW(), NOW(), NULL),
('Vercel', 'TECH_STACK', 'INFRA', NOW(), NOW(), NULL),
('Netlify', 'TECH_STACK', 'INFRA', NOW(), NOW(), NULL),

-- 가상화 및 오케스트레이션
('가상화', 'TECH_STACK', 'INFRA', NOW(), NOW(), NULL),
('오케스트레이션', 'TECH_STACK', 'INFRA', NOW(), NOW(), NULL),

-- 네트워크 및 성능
('리버스 프록시', 'TECH_STACK', 'INFRA', NOW(), NOW(), NULL),
('로드 밸런싱', 'TECH_STACK', 'INFRA', NOW(), NOW(), NULL),
('CDN', 'TECH_STACK', 'INFRA', NOW(), NOW(), NULL),

-- 보안 기술
('SSL/TLS/HTTPS', 'TECH_STACK', 'INFRA', NOW(), NOW(), NULL),
('DNS', 'TECH_STACK', 'INFRA', NOW(), NOW(), NULL),
('VPC', 'TECH_STACK', 'INFRA', NOW(), NOW(), NULL),
('방화벽', 'TECH_STACK', 'INFRA', NOW(), NOW(), NULL),
('WAF', 'TECH_STACK', 'INFRA', NOW(), NOW(), NULL),

-- 아키텍처 패턴 및 전략
('서버리스', 'TECH_STACK', 'INFRA', NOW(), NOW(), NULL),
('백업 및 복구 전략', 'TECH_STACK', 'INFRA', NOW(), NOW(), NULL);

-- ============================================
-- 📦 TOOL (도구 및 공통 기술) 태그 삽입
-- ============================================

INSERT INTO tags (name, tag_type, tag_category, created_at, updated_at, description)
VALUES
-- 버전 관리 도구
('Git', 'TECH_STACK', 'TOOL', NOW(), NOW(), NULL),
('GitHub', 'TECH_STACK', 'TOOL', NOW(), NOW(), NULL),
('GitLab', 'TECH_STACK', 'TOOL', NOW(), NOW(), NULL),
('Bitbucket', 'TECH_STACK', 'TOOL', NOW(), NOW(), NULL),

-- API 문서화/테스트 (백엔드에서 중복 제거)
('Postman', 'TECH_STACK', 'TOOL', NOW(), NOW(), NULL),
('Swagger', 'TECH_STACK', 'TOOL', NOW(), NOW(), NULL),
('OpenAPI', 'TECH_STACK', 'TOOL', NOW(), NOW(), NULL),

-- 보안 및 인증
('2FA', 'TECH_STACK', 'TOOL', NOW(), NOW(), NULL),

-- 테스트 도구
('Jest', 'TECH_STACK', 'TOOL', NOW(), NOW(), NULL),
('Vitest', 'TECH_STACK', 'TOOL', NOW(), NOW(), NULL),
('TestCode', 'TECH_STACK', 'TOOL', NOW(), NOW(), NULL),

-- 성능 분석 도구
('Lighthouse', 'TECH_STACK', 'TOOL', NOW(), NOW(), NULL),
('BundleAnalyzer', 'TECH_STACK', 'TOOL', NOW(), NOW(), NULL);

-- ============================================
-- 데이터 삽입 완료 - 검증 쿼리
-- ============================================

-- 카테고리별 태그 수 확인
SELECT tag_type,
       tag_category,
       COUNT(*) as count
FROM tags
GROUP BY tag_type, tag_category
ORDER BY tag_type, tag_category;

-- 전체 태그 수 확인
SELECT tag_type,
       COUNT(*) as total_count
FROM tags
GROUP BY tag_type;

-- 전체 태그 목록 확인 (이름별 정렬)
SELECT tag_id,
       name,
       tag_type,
       tag_category,
       created_at
FROM tags
ORDER BY tag_category, name;

-- ============================================
-- 예상 결과:
-- ERROR 태그: 10개 (10개 카테고리)
-- TECH_STACK 태그: 111개 (6개 카테고리)
-- 총 121개 태그
-- ============================================

COMMIT;
