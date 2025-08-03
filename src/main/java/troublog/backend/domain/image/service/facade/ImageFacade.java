package troublog.backend.domain.image.service.facade;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.image.service.s3.S3Uploader;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.ImageException;

/**
 * 썸네일 이미지, 유저 프로필 이미지 저장 및 삭제를 위한 클래스
 */
@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageFacade {
	public static final int DEFAULT_TIMEOUT_SECONDS = 30;
	private final S3Uploader s3Uploader;

	public CompletableFuture<String> saveSingleImage(MultipartFile image, String dirName) {
		return s3Uploader.uploadSingleImage(image, dirName)
			.orTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
	}

	public CompletableFuture<List<String>> saveMultipleImages(List<MultipartFile> images, String dirName) {
		return s3Uploader.uploadMultipleImages(images, dirName)
			.orTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
	}

	public CompletableFuture<Void> deleteSingleImage(String imageUrl) {
		return s3Uploader.deleteImage(imageUrl)
			.thenRun(() -> log.info("[Image] S3에서 이미지 제거 완료 | Image 주소: {}", imageUrl))
			.exceptionally(ex -> {
				throw new ImageException(ErrorCode.IMAGE_DELETE_FAILED);
			});
	}

	public CompletableFuture<Void> deleteMultipleImages(List<String> imageUrls) {
		return s3Uploader.deleteMultipleImages(imageUrls)
			.thenRun(() -> log.info("[Image] S3에서 다중 이미지 제거 완료 | 제거된 이미지 개수: {}", imageUrls.size()))
			.exceptionally(ex -> {
				throw new ImageException(ErrorCode.IMAGE_DELETE_FAILED);
			});
	}
}