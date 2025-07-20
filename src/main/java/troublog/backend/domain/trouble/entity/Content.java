package troublog.backend.domain.trouble.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import troublog.backend.domain.trouble.enums.ContentAuthorType;
import troublog.backend.domain.trouble.enums.ContentSummaryType;
import troublog.backend.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "content")
public class Content extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "content_id")
	private Long id;

	@Column(name = "sub_title")
	private String subTitle;

	@Column(columnDefinition = "TEXT")
	private String body;

	@NotNull
	@Column(name = "sequence")
	private int sequence;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "author_type")
	private ContentAuthorType authorType;

	@Enumerated(EnumType.STRING)
	@Column(name = "summary_type")
	private ContentSummaryType summaryType;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;
}
