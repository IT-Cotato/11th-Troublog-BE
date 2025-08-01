package troublog.backend.domain.user.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	public Long save(User user) {
		return userRepository.save(user).getId();
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

	public void deleteUser(User user) {
		user.deleteUser();
		log.info("[User] 사용자 삭제: userId={}", user.getId());
	}
}
