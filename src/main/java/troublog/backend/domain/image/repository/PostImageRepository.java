package troublog.backend.domain.image.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import troublog.backend.domain.image.entity.PostImage;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
	@Query("select p from PostImage p where p.post.id = ?1 and p.isThumbnail = true")
	Optional<PostImage> findPost_IdAndIsThumbnailTrue(Long postId);

	List<PostImage> findAllByPost_Id(Long postId);

	@Query("select p from PostImage p where p.post.id = ?1 and p.isThumbnail = false")
	List<PostImage> findAllByPost_IdAndIsThumbnailFalse(Long postId);

}