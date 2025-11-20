package troublog.backend.domain.user.service.command;

import java.util.List;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import troublog.backend.domain.user.entity.Follow;
import troublog.backend.domain.user.repository.FollowRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FollowCommandService {

	private final EntityManager entityManager;
	private final FollowRepository followRepository;

	public void save(Follow follow) {
		followRepository.save(follow);
		log.info("[Follow] 팔로우 등록: followerId={}, followingId={}", follow.getFollower().getId(),
			follow.getFollowing().getId());
	}

	public void delete(Follow follow) {
		followRepository.delete(follow);
		entityManager.flush();
		log.info("[Follow] 언팔로우 처리: followerId={}, followingId={}", follow.getFollower().getId(),
			follow.getFollowing().getId());
	}

	public void deleteAll(List<Follow> followList) {
		log.info("[Follow] 특정 사용자의 모든 팔로우/팔로잉 관계 삭제: followList={}", followList);
		followRepository.deleteAll(followList);
	}
}
