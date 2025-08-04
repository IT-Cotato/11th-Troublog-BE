package troublog.backend.domain.image.service.facade;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.image.service.s3.S3Uploader;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.ImageException;
import troublog.backend.global.common.error.exception.PostException;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageFacade {

	private static final int UPLOAD_TIMEOUT_SECONDS = 30;

	private final S3Uploader s3Uploader;

	public CompletableFuture<String> uploadSingleImage(Long userId, MultipartFile image, String dirName) {
		return s3Uploader.uploadSingleImage(userId, image, dirName)
			.orTimeout(UPLOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);
	}

	public CompletableFuture<List<String>> uploadMultipleImages(Long userId, List<MultipartFile> images,
		String dirName) {
		return s3Uploader.uploadMultipleImages(userId, images, dirName)
			.orTimeout(UPLOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS);
	}

	public CompletableFuture<Void> deleteSingleImage(String imageUrl) {
		return s3Uploader.deleteImage(imageUrl)
			.exceptionally(this::handleImageDeletionFailure);
	}

	public CompletableFuture<Void> deleteMultipleImages(List<String> imageUrls) {
		return s3Uploader.deleteMultipleImages(imageUrls)
			.exceptionally(this::handleImageDeletionFailure);
	}

	public DeferredResult<ResponseEntity<BaseResponse<String>>> uploadSingleImageAsync(
		Long userId, MultipartFile image, String dirName) {

		DeferredResult<ResponseEntity<BaseResponse<String>>> deferredResult = new DeferredResult<>();

		s3Uploader.uploadSingleImage(userId, image, dirName)
			.whenComplete((result, throwable) ->
				handleUploadCompletion(deferredResult, result, throwable));

		return deferredResult;
	}

	public DeferredResult<ResponseEntity<BaseResponse<List<String>>>> uploadMultipleImagesAsync(
		Long userId, List<MultipartFile> images, String dirName) {

		DeferredResult<ResponseEntity<BaseResponse<List<String>>>> deferredResult = new DeferredResult<>();

		uploadMultipleImages(userId, images, dirName)
			.whenComplete((result, throwable) ->
				handleMultipleUploadCompletion(deferredResult, result, throwable));

		return deferredResult;
	}

	private Void handleImageDeletionFailure(Throwable throwable) {
		throw new ImageException(ErrorCode.IMAGE_DELETE_FAILED);
	}

	private void handleUploadCompletion(
		DeferredResult<ResponseEntity<BaseResponse<String>>> deferredResult,
		String result,
		Throwable throwable
	) {

		if (throwable != null) {
			deferredResult.setErrorResult(throwable);
		} else {
			deferredResult.setResult(ResponseUtils.ok(result));
		}
	}

	private void handleMultipleUploadCompletion(
		DeferredResult<ResponseEntity<BaseResponse<List<String>>>> deferredResult,
		List<String> result,
		Throwable throwable
	) {

		if (throwable != null) {
			deferredResult.setErrorResult(throwable);
		} else {
			deferredResult.setResult(ResponseUtils.ok(result));
		}
	}
}