package troublog.backend.domain.trouble.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.domain.trouble.enums.SummaryType;
import troublog.backend.global.common.entity.BaseEntity;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.PostException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(
	name = "post_summaries",
	indexes = {
		@Index(name = "idx_post_summary_post_type", columnList = "post_id, summary_type"),
		@Index(name = "idx_post_summary_created", columnList = "created_at")
	}
)
public class PostSummary extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@Enumerated(EnumType.STRING)
	@Column(name = "summary_type")
	private SummaryType summaryType;

	@Builder.Default
	@OneToMany(mappedBy = "postSummary", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SummaryContent> summaryContents = new ArrayList<>();

	public void assignPost(Post post) {
		if (post == null) {
			throw new PostException(ErrorCode.MISSING_POST);
		}

		if (this.post != null) {
			this.post.getPostSummaries().remove(this);
		}
		this.post = post;
		if (!post.getPostSummaries().contains(this)) {
			post.getPostSummaries().add(this);
		}
	}

	public void addSummaryContents(SummaryContent summaryContent) {
		if (summaryContent == null) {
			throw new PostException(ErrorCode.MISSING_SUMMARY_CONTENT);
		}
		this.summaryContents.add(summaryContent);
		summaryContent.assignPostSummary(this);
	}
}
