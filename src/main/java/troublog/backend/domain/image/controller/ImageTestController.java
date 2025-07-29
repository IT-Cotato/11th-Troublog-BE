package troublog.backend.domain.image.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.image.service.facade.ImageFacade;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "이미지", description = "이미지 테스트 관련 엔드포인트")
public class ImageTestController {

	private final ImageFacade imageFacade;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "단일 이미지 업로드", description = "단일 이미지를 S3에 업로드한다.")
	public ResponseEntity<BaseResponse<String>> uploadImage(@RequestPart("multipartFile") MultipartFile file) {
		return ResponseUtils.ok(imageFacade.saveImage(file, "TEST"));
	}

	@DeleteMapping
	@Operation(summary = "단일 이미지 삭제", description = "S3 내부 이미지를 URL 값을 기반으로 삭제한다.")
	public ResponseEntity<BaseResponse<Void>> uploadImage(String imageUrl) {
		imageFacade.deleteImage(imageUrl);
		return ResponseUtils.noContent();
	}

}
