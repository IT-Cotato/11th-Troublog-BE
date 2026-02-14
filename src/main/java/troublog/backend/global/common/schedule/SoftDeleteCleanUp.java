package troublog.backend.global.common.schedule;

import java.time.LocalDateTime;
import java.util.function.IntSupplier;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.project.repository.ProjectRepository;
import troublog.backend.domain.trouble.repository.CommentRepository;
import troublog.backend.domain.trouble.repository.ContentRepository;
import troublog.backend.domain.trouble.repository.PostRepository;
import troublog.backend.domain.trouble.repository.PostTagRepository;
import troublog.backend.domain.user.repository.UserRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class SoftDeleteCleanUp {

	private static final long RETENTION_MONTHS = 1L;

	private final PostTagRepository postTagRepository;
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final ContentRepository contentRepository;
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;

	// 매일 오전 4시에 돌아가는 배치
	@Scheduled(cron = "0 0 4 * * *")
	@Transactional
	public void cleanUpSoftDeletedRows() {
		LocalDateTime threshold = LocalDateTime.now().minusMonths(RETENTION_MONTHS);
		log.info("[SoftDeleteCleanUp] 시작 - softDelete된지 한달이 넘은 데이터 모두 삭제={}", threshold);

		int totalDeleted = 0;
		try {
			totalDeleted += clean("PostTag", () -> postTagRepository.deleteAllSoftDeletedBefore(threshold));
			totalDeleted += clean("Comment", () -> commentRepository.deleteAllSoftDeletedBefore(threshold));
			totalDeleted += clean("Contents", () -> contentRepository.deleteAllSoftDeletedBefore(threshold));
			totalDeleted += clean("Post", () -> postRepository.deleteAllSoftDeletedBefore(threshold));
			totalDeleted += clean("Project", () -> projectRepository.deleteAllSoftDeletedBefore(threshold));
			totalDeleted += clean("User", () -> userRepository.deleteAllSoftDeletedBefore(threshold));
		} catch (Exception e) {
			log.error("[SoftDeleteCleanUp] 참조 무결성에 어긋나는 데이터 존재: error={}", e.getMessage());
			throw e;
		}
		log.info("[SoftDeleteCleanUp] 종료 - totalDeleted={}", totalDeleted);
	}

	private int clean(String targetName, IntSupplier action) {
		int deletedCount = action.getAsInt();
		if (deletedCount > 0) {
			log.info("[SoftDeleteCleanUp] hardDelete - target={}, count={}", targetName, deletedCount);
		}
		return deletedCount;
	}
}
