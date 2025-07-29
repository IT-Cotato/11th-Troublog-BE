package troublog.backend.domain.image.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.image.entity.PostImage;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
	Optional<PostImage> findByPost_IdAndIsThumbnailTrue(Long postId);

	List<PostImage> findAllByPost_Id(Long postId);

	List<PostImage> findAllByPost_IdAndIsThumbnailFalse(Long postId);

}