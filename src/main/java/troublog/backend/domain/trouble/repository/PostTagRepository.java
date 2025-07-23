package troublog.backend.domain.trouble.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostTag;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    List<PostTag> findByPost(Post post);

    List<PostTag> findByPostId(Long postId);
}