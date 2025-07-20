package troublog.backend.domain.community.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "feed")
public class Feed extends BaseEntity {

	//TODO Post visibility = true일때 Feed 가 생성되고 업데이트 되도록 작성 필요

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_feed_id")
	private Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "body_preview", columnDefinition = "TEXT")
	private String bodyPreview;

	@Column(name = "thumbnail_url", length = 255)
	private String thumbnailUrl;

	@NotNull
	@Column(name = "like_cnt")
	private int likeCount;

	@NotNull
	@Column(name = "comment_cnt")
	private int commentCount;

	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", unique = true)
	private Post post;
}
