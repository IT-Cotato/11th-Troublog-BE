package troublog.backend.domain.trouble.entity;

import jakarta.validation.constraints.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "summary_contents")
public class SummaryContent extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "summary_content_id")
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_summary_id")
	private PostSummary postSummary;

	@Column(name = "sub_title")
	private String subTitle;

	@NotNull
	@Column(columnDefinition = "TEXT")
	private String body;

	@NotNull
	@Column(name = "sequence")
	private Integer sequence;

	public void assignPostSummary(PostSummary postSummary) {
		if (postSummary == null) {
			throw new PostException(ErrorCode.MISSING_POST_SUMMARY);
		}
		if (this.postSummary != null) {
			this.postSummary.getSummaryContents().remove(this);
		}
		this.postSummary = postSummary;
		if (!postSummary.getSummaryContents().contains(this)) {
			postSummary.getSummaryContents().add(this);
		}
	}
}