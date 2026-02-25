package troublog.backend.domain.user.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.user.dto.request.UserProfileUpdateReqDto;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.repository.UserRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService {

	private final UserRepository userRepository;

	public User save(User user) {
		return userRepository.save(user);
	}

	public void updateUserProfile(User user, UserProfileUpdateReqDto userProfileUpdateReqDto) {
		user.updateUserProfile(userProfileUpdateReqDto);
		log.info("[User] 프로필 업데이트: nickname={}, field={}, bio={}, githubUrl={}, profileUrl={}",
			userProfileUpdateReqDto.nickname(),
			userProfileUpdateReqDto.field(),
			userProfileUpdateReqDto.bio(),
			userProfileUpdateReqDto.githubUrl(),
			userProfileUpdateReqDto.profileUrl()
		);
	}

	public void softDeleteUser(User user) {
		userRepository.delete(user);
		log.info("[User] 유저 논리 삭제: userId={}", user.getId());
	}
}
