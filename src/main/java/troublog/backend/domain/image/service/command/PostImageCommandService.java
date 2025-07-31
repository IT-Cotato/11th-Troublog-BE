package troublog.backend.domain.image.service.command;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.image.entity.PostImage;
import troublog.backend.domain.image.repository.PostImageRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImageCommandService {

	private final PostImageRepository postImageRepository;

	public PostImage save(PostImage postImage) {
		PostImage savedImage = postImageRepository.save(postImage);
		log.info("[Image] 이미지 엔티티 저장 완료: postImageId={}", savedImage.getId());
		return savedImage;
	}

	public List<PostImage> saveAll(List<PostImage> postImageList) {
		List<PostImage> savedImages = postImageRepository.saveAll(postImageList);
		log.info("[Image] 다중 이미지 엔티티 저장 완료: savedCount={}", savedImages.size());
		return savedImages;
	}

	public void delete(PostImage postImage) {
		postImageRepository.delete(postImage);
		log.info("[Image] 이미지 삭제 완료: postImageId={}", postImage.getId());
	}

	public void deleteAll(List<PostImage> postImageList) {
		postImageRepository.deleteAll(postImageList);
		log.info("[Image] 다중 이미지 삭제 완료: deletedCount={}", postImageList.size());
	}
}
