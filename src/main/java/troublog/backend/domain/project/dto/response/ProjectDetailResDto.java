package troublog.backend.domain.project.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "프로젝트 상세 조회 응답 DTO")
public record ProjectDetailResDto(

	@Schema(description = "프로젝트 id")
	Long id,

	@Schema(description = "프로젝트 이름")
	String name,

	@Schema(description = "한 줄 소개")
	String description,

	@Schema(description = "썸네일 이미지 URL")
	String thumbnailImageUrl,

	@Schema(description = "기술 태그들")
	List<String> tags
) {
}

