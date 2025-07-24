package troublog.backend.domain.like.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import troublog.backend.domain.like.dto.response.LikePostResDto;
import troublog.backend.domain.like.service.LikeService;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "마이페이지", description = "사용자 통계")
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/likes")
    @Operation(summary = "좋아요한 포스트 API", description = "최근에 좋아요한 순으로 포스트를 불러온다.")
    public ResponseEntity<BaseResponse<List<LikePostResDto>>> getUserLikedPosts(
            @AuthenticationPrincipal CustomAuthenticationToken auth) {
        Long userId = auth.getUserId();
        List<LikePostResDto> likedPosts = likeService.getLikedPostsByUser(userId);
        return ResponseUtils.created(likedPosts);
    }
}
