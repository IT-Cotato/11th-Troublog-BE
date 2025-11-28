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
import jakarta.validation.constraints.NotNull;
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
	name = "contents",
	indexes = {
		@Index(
				name = "idx_contents_post_id",
				columnList = "post_id"
			)
	}
)
public class Content extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "content_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@Column(name = "sub_title")
	private String subTitle;

	@Column(columnDefinition = "TEXT")
	private String body;

	@NotNull
	@Column(name = "sequence")
	private int sequence;

	// 연관관계 편의 메서드
	public void assignPost(Post post) {
		if (post == null) {
			throw new PostException(ErrorCode.MISSING_POST);
		}
		if (this.post != null) {
			this.post.getContents().remove(this);
		}
		this.post = post;
		if (!post.getContents().contains(this)) {
			post.getContents().add(this);
		}
	}
}
