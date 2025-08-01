package troublog.backend.domain.trouble.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.domain.trouble.enums.TagCategory;
import troublog.backend.domain.trouble.enums.TagType;
import troublog.backend.global.common.entity.BaseEntity;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(
	name = "tags",
	indexes = {
		@Index(
			name = "idx_tags_name",
			columnList = "name"
		)
	}
)
public class Tag extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tag_id")
	private Long id;

	@NotNull
	@Column(unique = true)
	private String name;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "tag_type")
	private TagType tagType;

	private String description;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "tag_category")
	private TagCategory tagCategory;

	@Builder.Default
	@OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostTag> postTags = new ArrayList<>();

	public boolean isSameType(TagType tagType) {
		return this.tagType == tagType;
	}

	public void addPostTag(PostTag postTag) {
		if (postTag == null) {
			throw new PostException(ErrorCode.MISSING_POST_TAG);
		}
		if (this.postTags.contains(postTag)) {
			return;
		}
		this.postTags.add(postTag);
		postTag.assignTag(this);
	}
}
