package troublog.backend.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.user.entity.Follow;
import troublog.backend.domain.user.repository.FollowRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowCommandService {

	private final FollowRepository followRepository;

	public void save(Follow follow) {
		followRepository.save(follow);
	}

	public void delete(Follow follow) {
		followRepository.delete(follow);
	}
}
