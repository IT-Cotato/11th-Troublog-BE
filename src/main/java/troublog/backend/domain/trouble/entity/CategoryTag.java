package troublog.backend.domain.trouble.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "category_tag")
public class CategoryTag extends BaseEntity {

	@OneToMany(mappedBy = "post_tag", cascade = CascadeType.ALL, orphanRemoval = true)
	List<PostTag> postTags;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_tag_id")
	private Long id;
	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "description", nullable = false, columnDefinition = "TEXT")
	private String description;
}