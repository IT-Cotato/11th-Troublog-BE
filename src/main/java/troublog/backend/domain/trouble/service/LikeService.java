package troublog.backend.domain.trouble.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.converter.LikePostConverter;
import troublog.backend.domain.trouble.dto.response.LikePostResDto;
import troublog.backend.domain.trouble.entity.Like;
import troublog.backend.domain.trouble.repository.LikeRepository;

@Service
@RequiredArgsConstructor
public class LikeService {
	private final LikeRepository likeRepository;

	public List<LikePostResDto> getLikedPostsByUser(Long userId) {
		List<Like> likes = likeRepository.findByUserIdOrderByLikedAtDesc(userId);

		if (likes.isEmpty()) {
			return Collections.emptyList();
		}

		return likes.stream()
			.map(like -> LikePostConverter.toResponse(like.getPost()))
			.collect(Collectors.toList());
	}

}
