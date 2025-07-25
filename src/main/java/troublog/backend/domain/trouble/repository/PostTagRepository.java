package troublog.backend.domain.trouble.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.trouble.entity.PostTag;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
	List<PostTag> findAllByPostId(Long postId);
}