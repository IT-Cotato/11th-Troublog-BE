package troublog.backend.domain.image.service.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.image.entity.PostImage;
import troublog.backend.domain.image.repository.PostImageRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.ImageException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImageQueryService {

	private final PostImageRepository postImageRepository;

	public PostImage findById(Long postImageId) {
		PostImage postImage = postImageRepository.findById(postImageId)
			.orElseThrow(() -> new ImageException(ErrorCode.IMAGE_NOT_FOUND));
		log.info("[Image] 단일 이미지 조회 완료: postImageId={}", postImage.getId());
		return postImage;
	}

	public List<PostImage> findAllByPostId(Long postId) {
		List<PostImage> postImages = postImageRepository.findAllByPost_Id(postId);
		log.info("[Image] 모든 이미지 조회 완료: postId={}, imageCount={}", postId, postImages.size());
		return postImages;
	}

	public PostImage findThumbnailsByPostId(Long postId) {
		PostImage postImage = postImageRepository.findPost_IdAndIsThumbnailTrue(postId)
			.orElseThrow(() -> new ImageException(ErrorCode.IMAGE_NOT_FOUND));
		log.info("[Image] 썸네일 조회 완료: postId={}, postImageId={}", postId, postImage.getId());
		return postImage;
	}

	public List<PostImage> findNonThumbnailsByPostId(Long postId) {
		List<PostImage> nonThumbnails = postImageRepository.findAllByPost_IdAndIsThumbnailFalse(postId);
		log.info("[Image] 일반 이미지 조회 완료: postId={}, imageCount={}", postId, nonThumbnails.size());
		return nonThumbnails;
	}
}
