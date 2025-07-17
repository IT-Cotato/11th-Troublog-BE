package troublog.backend.domain.image.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import troublog.backend.domain.image.enums.ThumbnailDomainType;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.global.common.entity.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "thumbnail_image")
public class ThumbnailImage extends BaseEntity {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")  // 외래키 설정
	Post post;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")  // 외래키 설정
	Project project;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "thumbnail_image_id")
	private Long id;
	@NonNull
	private String url;

	@Enumerated
	private ThumbnailDomainType thumbnailDomainType;
}
