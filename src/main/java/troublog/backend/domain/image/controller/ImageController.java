package troublog.backend.domain.image.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.image.service.facade.ImageFacade;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "이미지", description = "이미지 관련 엔드포인트")
public class ImageController {

	private final ImageFacade imageFacade;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "단일 이미지 업로드", description = "단일 이미지를 S3에 업로드한다.")
	public ResponseEntity<BaseResponse<String>> uploadSingleImage(
		@RequestPart("multipartFile") MultipartFile image,
		@Schema(name = "폴더 이름") String dirName
	) {
		return ResponseUtils.ok(imageFacade.saveSingleImage(image, dirName));
	}

	@PostMapping(path = "/multi", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "다중 이미지 업로드", description = "다중 이미지를 S3에 업로드한다.")
	public ResponseEntity<BaseResponse<List<String>>> uploadMultipleImage(
		@RequestPart("multipartFile") List<MultipartFile> images,
		@Schema(name = "폴더 이름") String dirName
	) {
		return ResponseUtils.ok(imageFacade.saveMultipleImages(images, dirName));
	}

	@DeleteMapping
	@Operation(summary = "단일 이미지 삭제", description = "S3에 저장된 단일 이미지를 URL 값을 기반으로 삭제한다.")
	public ResponseEntity<BaseResponse<Void>> deleteSingleImage(@RequestParam String imageUrl) {
		imageFacade.deleteSingleImage(imageUrl);
		return ResponseUtils.noContent();
	}

	@DeleteMapping("/multi")
	@Operation(summary = "다중 이미지 삭제", description = "S3에 저장된 다중 이미지를 URL 값을 기반으로 삭제한다.")
	public ResponseEntity<BaseResponse<Void>> deleteMultipleImages(@RequestParam List<String> imageUrls) {
		imageFacade.deleteMultipleImages(imageUrls);
		return ResponseUtils.noContent();
	}

}

