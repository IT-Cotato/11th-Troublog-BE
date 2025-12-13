package troublog.backend.global.common.batch;

import java.time.LocalDateTime;
import java.util.function.IntSupplier;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.project.repository.ProjectRepository;
import troublog.backend.domain.trouble.repository.CommentRepository;
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
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;

	// 매일 오전 1시에 돌아가는 배치
	@Scheduled(cron = "0 0 1 * * *")
	@Transactional
	public void cleanUpSoftDeletedRows() {
		LocalDateTime threshold = LocalDateTime.now().plusMonths(RETENTION_MONTHS);
		log.info("[SoftDeleteCleanUp] 시작 - softDelete된지 한달이 넘은 데이터 모두 삭제={}", threshold);

		int totalDeleted = 0;
		totalDeleted += clean("PostTag", () -> postTagRepository.deleteAllSoftDeletedBefore(threshold));
		totalDeleted += clean("Comment", () -> commentRepository.deleteAllSoftDeletedBefore(threshold));
		totalDeleted += clean("Post", () -> postRepository.deleteAllSoftDeletedBefore(threshold));
		totalDeleted += clean("Project", () -> projectRepository.deleteAllSoftDeletedBefore(threshold));
		totalDeleted += clean("User", () -> userRepository.deleteAllSoftDeletedBefore(threshold));

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
