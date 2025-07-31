package troublog.backend.domain.like.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import troublog.backend.domain.like.converter.LikePostConverter;
import troublog.backend.domain.like.dto.response.LikePostResDto;
import troublog.backend.domain.like.entity.Like;
import troublog.backend.domain.like.repository.LikeRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
