package troublog.backend.domain.trouble.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.global.common.entity.BaseEntity;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(
	name = "post_tags",
	indexes = {
		@Index(
			name = "idx_post_tags_post_id",
			columnList = "post_id"
		),
		@Index(
			name = "idx_post_tags_tag_id",
			columnList = "tag_id"
		),
		// 복합 인덱스 (유니크 제약조건도 함께)
		@Index(
			name = "idx_post_tags_composite",
			columnList = "post_id, tag_id",
			unique = true
		)
	}
)
public class PostTag extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_tag_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_id")
	private Tag tag;

	// 연관관계 편의 메서드
	public void assignPost(Post post) {
		if (post == null) {
			throw new PostException(ErrorCode.MISSING_POST);
		}
		if (this.post != null) {
			this.post.getPostTags().remove(this);
		}
		this.post = post;
		if (!post.getPostTags().contains(this)) {
			post.getPostTags().add(this);
		}
	}

	public void assignTag(Tag tag) {
		if (tag == null) {
			throw new PostException(ErrorCode.MISSING_TAG);
		}
		if (this.tag != null) {
			this.tag.getPostTags().remove(this);
		}
		this.tag = tag;
		if (!tag.getPostTags().contains(this)) {
			tag.getPostTags().add(this);
		}
	}
}