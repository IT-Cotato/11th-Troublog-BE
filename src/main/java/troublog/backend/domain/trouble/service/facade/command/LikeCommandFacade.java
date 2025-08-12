package troublog.backend.domain.trouble.service.facade.command;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
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
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Service
@RequiredArgsConstructor
public class LikeCommandFacade {
	private final PostQueryService postQueryService;
	private final UserQueryService userQueryService;
	private final LikeQueryService likeQueryService;
	private final LikeCommandService likeCommandService;

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

		if (likeQueryService.findByUserAndPost(userId, postId).isPresent()) {
			throw new PostException(ErrorCode.LIKE_ALREADY_EXISTS);
		}

		Like like = Like.createLike(user, post);
		Like saved = likeCommandService.save(like);
		return LikeConverter.toResponse(saved);
	}

	@Transactional
	public void deleteLike(Long postId, Long userId) {
		Post post = postQueryService.findById(postId);
		PostValidator.validateVisibility(post);

		Like like = likeQueryService.findByUserAndPost(userId, postId)
			.orElseThrow(() -> new PostException(ErrorCode.LIKE_NOT_EXISTS));

		likeCommandService.deleteLike(like);
	}
}
