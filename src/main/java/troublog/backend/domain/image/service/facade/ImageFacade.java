package troublog.backend.domain.image.service.facade;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
	private final S3Uploader s3Uploader;

	public String saveImage(MultipartFile file, String dirName) {
		try {
			return s3Uploader.uploadSingleImage(file, dirName)
				.get(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new ImageException(ErrorCode.IMAGE_UPLOAD_FAILED);
		} catch (ExecutionException | TimeoutException e) {
			throw new ImageException(ErrorCode.IMAGE_UPLOAD_FAILED);
		}
	}

	public CompletableFuture<Void> deleteImage(String imageUrl) {
		return s3Uploader.deleteImage(imageUrl)
			.exceptionally(ex -> {
				throw new ImageException(ErrorCode.IMAGE_DELETE_FAILED);
			});
	}
}