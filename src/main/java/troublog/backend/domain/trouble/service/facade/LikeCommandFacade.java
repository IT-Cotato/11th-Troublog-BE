package troublog.backend.domain.trouble.service.facade;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.converter.LikeConverter;
import troublog.backend.domain.trouble.dto.response.LikePostResDto;
import troublog.backend.domain.trouble.dto.response.LikeResDto;
import troublog.backend.domain.trouble.entity.Like;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.repository.LikeRepository;
import troublog.backend.domain.trouble.service.command.LikeCommandService;
import troublog.backend.domain.trouble.service.factory.PostFactory;
import troublog.backend.domain.trouble.service.query.LikeQueryService;
import troublog.backend.domain.trouble.service.query.PostQueryService;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.service.query.UserQueryService;

@Service
@RequiredArgsConstructor
public class LikeCommandFacade {
	private final LikeRepository likeRepository;
	private final PostQueryService postQueryService;
	private final UserQueryService userQueryService;
	private final LikeQueryService likeQueryService;
	private final LikeCommandService likeCommandService;

	public List<LikePostResDto> getLikedPostsByUser(Long userId) {
		List<Like> likes = likeRepository.findByUserIdOrderByLikedAtDesc(userId);

		if (likes.isEmpty()) {
			return Collections.emptyList();
		}

		return likes.stream()
			.map(like -> LikeConverter.toListResponse(like.getPost()))
			.collect(Collectors.toList());
	}

	public LikeResDto postLike(Long postId, Long userId) {
		Post post = postQueryService.findById(postId);
		PostFactory.validateVisibility(post);
		User user = userQueryService.findUserById(userId);
		likeQueryService.assertLikeNotExists(user, post); // 중복 좋아요 검증

		Like newLike = likeCommandService.save(Like.createLike(user, post));
		return LikeConverter.toResponse(newLike);
	}
}
