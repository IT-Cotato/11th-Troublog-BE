package troublog.backend.domain.trouble.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.trouble.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	Optional<Post> findByIdAndIsDeletedFalse(Long id);

    List<Post> findByIsDeletedFalse();

    List<Post> findByIsDeletedTrue();
}