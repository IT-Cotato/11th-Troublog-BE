package troublog.backend.domain.like.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Schema(description = "좋아요한 포스트 응답 DTO")
public record LikePostResDto(
        @Schema(description = "게시글 ID")
        Long id,

        @Schema(description = "게시글 제목")
        String title,

        @Schema(description = "에러 태그")
        String errorTags,

        @Schema(description = "기술 태그 목록")
        List<String> techTags,

        @Schema(description = "게시글 내용")
        List<String> contents,

        @Schema(description = "좋아요 수")
        int likeCount,

        @Schema(description = "댓글 수")
        int commentCount,

        @Schema(description = "작성 날짜")
        LocalDateTime createdAt,

        @Schema(description = "첨부 이미지")
        List<String> images
) {
}
