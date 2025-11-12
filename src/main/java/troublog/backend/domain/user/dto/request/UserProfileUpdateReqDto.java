package troublog.backend.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "사용자 프로필 수정 요청 DTO")
public record UserProfileUpdateReqDto(

	@Schema(description = "유저 아이디")
	Long userId,

	@Schema(description = "닉네임")
	String nickname,

	@Schema(description = "분야")
	String field,

	@Schema(description = "한 줄 소개")
	String bio,

	@Schema(description = "깃허브 주소")
	String githubUrl,

	@Schema(description = "프로필 이미지 주소")
	String profileUrl
) {
}
