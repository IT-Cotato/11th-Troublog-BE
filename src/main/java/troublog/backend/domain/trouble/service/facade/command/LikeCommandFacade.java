package troublog.backend.domain.trouble.service.facade.command;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.alert.converter.AlertConverter;
import troublog.backend.domain.alert.dto.response.AlertResDto;
import troublog.backend.domain.alert.entity.Alert;
import troublog.backend.domain.alert.service.AlertCommandService;
import troublog.backend.domain.trouble.converter.LikeConverter;
import troublog.backend.domain.trouble.dto.response.LikePostResDto;
import troublog.backend.domain.trouble.dto.response.LikeResDto;
import troublog.backend.domain.trouble.entity.Like;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.service.command.LikeCommandService;
import troublog.backend.domain.trouble.service.query.LikeQueryService;
import troublog.backend.domain.trouble.service.query.PostQueryService;
import troublog.backend.domain.trouble.validator.PostValidator;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.service.query.UserQueryService;
import troublog.backend.global.common.util.AlertSseUtil;

@Service
@RequiredArgsConstructor
public class LikeCommandFacade {
	private final PostQueryService postQueryService;
	private final UserQueryService userQueryService;
	private final LikeQueryService likeQueryService;
	private final LikeCommandService likeCommandService;
	private final AlertCommandService alertCommandService;

	private final AlertSseUtil alertSseUtil;

	@Transactional(readOnly = true)
	public Page<LikePostResDto> getLikedPostsByUser(Long userId, Pageable pageable) {
		Page<Like> likes = likeQueryService.findByUserIdOrderByLikedAtDesc(userId, pageable);
		return likes.map(like -> LikeConverter.toListResponse(like.getPost()));
	}

	@Transactional
	public LikeResDto postLike(Long postId, Long userId) {
		Post post = postQueryService.findById(postId);
		PostValidator.validateVisibility(post);
		User user = userQueryService.findUserById(userId);

		Optional<Like> existing = likeQueryService.findByUserAndPost(userId, postId);

		if (existing.isPresent()) {
			// 좋아요 취소
			LikeResDto res = LikeConverter.toResponse(existing.get(), false);
			deleteLike(existing.get());
			return res;
		}
		Like like = Like.createLike(user, post);
		Like saved = likeCommandService.save(like);

		// 좋아요 알림 전송
		Alert alert = AlertConverter.postLikesAlert(post.getUser(), user.getNickname());
		AlertResDto alertResDto = AlertConverter.convertToAlertResDto(alert);

		if (alertSseUtil.sendAlert(post.getUser().getId(), alertResDto)) {
			alert.markAsSent();
		}

		alertCommandService.save(alert);

		return LikeConverter.toResponse(saved, true);

	}

	@Transactional
	public void deleteLike(Like like) {
		likeCommandService.deleteLike(like);
	}
}
