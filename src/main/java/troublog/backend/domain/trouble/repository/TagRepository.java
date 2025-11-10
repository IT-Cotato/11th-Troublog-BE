package troublog.backend.domain.trouble.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import troublog.backend.domain.trouble.entity.Tag;
import troublog.backend.domain.trouble.enums.TagType;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

	Optional<Tag> findTagByNameAndTagType(String name, TagType tagType);

	Optional<Tag> findTagByNormalizedNameAndTagType(String normalizedName, TagType tagType);

	List<Tag> findTagByNameContaining(String name);

	List<Tag> findByNormalizedNameInAndTagType(List<String> normalizedNames, TagType tagType);

	List<Tag> findByNameInAndTagType(List<String> names, TagType tagType);
}