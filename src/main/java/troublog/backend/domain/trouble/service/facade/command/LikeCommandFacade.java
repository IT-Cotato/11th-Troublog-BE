package troublog.backend.domain.trouble.service.facade.command;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.converter.LikeConverter;
import troublog.backend.domain.trouble.dto.response.LikePostResDto;
import troublog.backend.domain.trouble.dto.response.LikeResDto;
import troublog.backend.domain.trouble.entity.Like;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.service.command.LikeCommandService;
import troublog.backend.domain.trouble.service.factory.PostFactory;
import troublog.backend.domain.trouble.service.query.LikeQueryService;
import troublog.backend.domain.trouble.service.query.PostQueryService;
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
	public List<LikePostResDto> getLikedPostsByUser(Long userId) {
		List<Like> likes = likeQueryService.findByUserIdOrderByLikedAtDesc(userId);

		if (likes.isEmpty()) {
			return Collections.emptyList();
		}

		return likes.stream()
			.map(like -> LikeConverter.toListResponse(like.getPost()))
			.collect(Collectors.toList());
	}

	@Transactional
	public LikeResDto postLike(Long postId, Long userId) {
		Post post = postQueryService.findById(postId);
		PostFactory.validateVisibility(post);
		User user = userQueryService.findUserById(userId);

		if (likeQueryService.findByUserAndPost(userId, postId).isPresent()) {
			throw new PostException(ErrorCode.LIKE_ALREADY_EXISTS);
		}

		Like newLike = likeCommandService.save(Like.createLike(user, post));
		return LikeConverter.toResponse(newLike);
	}

	@Transactional
	public void deleteLike(Long postId, Long userId) {
		Post post = postQueryService.findById(postId);
		PostFactory.validateVisibility(post);

		Like like = likeQueryService.findByUserAndPost(userId, postId)
			.orElseThrow(() -> new PostException(ErrorCode.LIKE_NOT_EXISTS));

		likeCommandService.deleteLike(like);
	}
}
