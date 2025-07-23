package troublog.backend.domain.trouble.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.trouble.entity.Tag;
import troublog.backend.domain.trouble.enums.TagType;

public interface TagRepository extends JpaRepository<Tag, Long> {

	Optional<Tag> findByName(String name);

	List<Tag> findByNameInAndTagType(List<String> names, TagType tagType);
}