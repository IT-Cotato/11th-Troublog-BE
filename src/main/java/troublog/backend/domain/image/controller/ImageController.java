package troublog.backend.domain.image.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.image.service.facade.ImageFacade;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@Slf4j
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "이미지 관리", description = "S3 이미지 업로드/삭제 API")
public class ImageController {

	private final ImageFacade imageFacade;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "단일 이미지 업로드", description = "단일 이미지를 S3에 업로드한다.")
	public ResponseEntity<BaseResponse<String>> uploadSingleImage(
		@Schema(description = "업로드할 이미지 파일")
		@RequestPart("multipartFile") MultipartFile image,
		@Schema(
			description = """
				S3 폴더명 (선택사항)
				- 제공 시: 지정된 폴더명 사용
				- 미제공 시: '{userId}-{YYYY-MM-DD}' 형식으로 자동 생성
				"""
		)
		@RequestParam(required = false) String dirName
	) {
		String response = imageFacade.uploadSingleImageAsync(image, dirName);
		return ResponseUtils.ok(response);
	}

	@PostMapping(path = "/multi", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "다중 이미지 업로드", description = "다중 이미지를 S3에 업로드한다.")
	public ResponseEntity<BaseResponse<List<String>>> uploadMultipleImage(
		@Schema(description = "업로드할 이미지 파일 목록")
		@RequestPart("multipartFile") List<MultipartFile> images,
		@Schema(
			description = """
				S3 폴더명 (선택사항)
				- 제공 시: 지정된 폴더명 사용
				- 미제공 시: '{userId}-{YYYY-MM-DD}' 형식으로 자동 생성
				"""
		)
		@RequestParam(required = false) String dirName
	) {
		List<String> response = imageFacade.uploadMultipleImagesAsync(images, dirName);
		return ResponseUtils.ok(response);
	}

	@DeleteMapping
	@Operation(summary = "단일 이미지 삭제", description = "S3에 저장된 단일 이미지를 URL 값을 기반으로 삭제한다.")
	public ResponseEntity<BaseResponse<Void>> deleteSingleImage(@RequestParam String imageUrl) {
		imageFacade.deleteSingleImage(imageUrl).join();
		return ResponseUtils.noContent();
	}

	@DeleteMapping("/multi")
	@Operation(summary = "다중 이미지 삭제", description = "S3에 저장된 다중 이미지를 URL 값을 기반으로 삭제한다.")
	public ResponseEntity<BaseResponse<Void>> deleteMultipleImages(@RequestParam List<String> imageUrls) {
		imageFacade.deleteMultipleImages(imageUrls).join();
		return ResponseUtils.noContent();
	}

}