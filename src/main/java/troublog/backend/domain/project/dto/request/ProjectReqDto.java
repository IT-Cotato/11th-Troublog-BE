package troublog.backend.domain.project.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "프로젝트 생성/수정 요청 DTO")
public record ProjectReqDto(

	@Schema(description = "프로젝트 이름")
	@NotNull(message = "프로젝트 이름은 null 값일 수 없습니다.")
	String name,

	@Schema(description = "한 줄 소개")
	String description,

	@Schema(description = "썸네일 이미지 URL")
	String thumbnailImageUrl

) {
}
