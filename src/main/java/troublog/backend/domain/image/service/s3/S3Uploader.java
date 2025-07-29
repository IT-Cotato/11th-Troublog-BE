package troublog.backend.domain.image.service.s3;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.image.validator.ImageValidator;
import troublog.backend.global.common.config.property.AwsProperties;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.ImageException;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {
	private static final long DEFAULT_TIMEOUT_SECONDS = 60;
	private static final String AWS_S3_DOMAIN = "amazonaws.com/";
	private static final String PATH_SEPARATOR = "/";

	private final S3Template s3Template;
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;
	private final AwsProperties awsProperties;
	private final Executor imageUploadExecutor;

	/**
	 * 단일 이미지 파일을 AWS S3에 비동기적으로 업로드합니다.
	 *
	 * @param file    업로드할 MultipartFile 객체
	 * @param dirName S3에 업로드될 디렉토리 이름
	 * @return 업로드된 파일의 S3 URL을 포함하는 CompletableFuture
	 * @throws ImageException 파일이 유효하지 않거나 업로드 실패 시 발생
	 */
	public CompletableFuture<String> uploadSingleImage(MultipartFile file, String dirName) {
		return CompletableFuture.supplyAsync(
				() -> {
					ImageValidator.validate(file);
					return executeUpload(file, dirName);
				},
				imageUploadExecutor
			)
			.thenApply(result -> {
				log.info("[S3] 파일 업로드 완료: {}", file.getOriginalFilename());
				return result;
			});
	}

	/**
	 * 여러 이미지 파일을 AWS S3에 비동기적으로 업로드합니다.
	 *
	 * @param fileList 업로드할 MultipartFile 객체 목록
	 * @param dirName  S3에 업로드될 디렉토리 이름
	 * @return 업로드된 모든 파일의 S3 URL 목록을 포함하는 CompletableFuture
	 * @throws ImageException 파일 목록이 유효하지 않거나 업로드 실패 시 발생
	 */
	public CompletableFuture<List<String>> uploadMultipleImages(List<MultipartFile> fileList, String dirName) {
		ImageValidator.validateFileList(fileList);

		List<CompletableFuture<String>> uploadFutures = fileList.stream()
			.map(file -> uploadSingleFileInternal(file, dirName))
			.toList();

		return CompletableFuture.allOf(uploadFutures.toArray(CompletableFuture[]::new))
			.thenApply(v -> uploadFutures.stream()
				.map(CompletableFuture::join)
				.toList())
			.thenApply(result -> {
				log.info("[S3] 다중 파일 업로드 완료: {}개", result.size());
				return result;
			})
			.orTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
	}

	/**
	 * S3에 저장된 이미지를 비동기적으로 삭제합니다.
	 *
	 * @param s3Url 삭제할 이미지의 S3 URL
	 * @return 삭제 작업 완료를 나타내는 CompletableFuture
	 * @throws ImageException URL이 유효하지 않거나 삭제 실패 시 발생
	 */
	public CompletableFuture<Void> deleteImage(String s3Url) {
		return CompletableFuture.runAsync(
				() -> {
					ImageValidator.validateS3Url(s3Url);
					executeDelete(s3Url);
				},
				imageUploadExecutor
			)
			.thenRun(() -> log.info("[S3] 파일 삭제 완료"));
	}

	/**
	 * S3에 저장된 여러 이미지를 비동기적으로 삭제합니다.
	 *
	 * @param s3Urls 삭제할 이미지들의 S3 URL 목록
	 * @return 모든 삭제 작업 완료를 나타내는 CompletableFuture
	 * @throws ImageException URL이 유효하지 않거나 삭제 실패 시 발생
	 */
	public CompletableFuture<Void> deleteMultipleImages(List<String> s3Urls) {
		List<CompletableFuture<Void>> deleteFutures = s3Urls.stream()
			.filter(StringUtils::hasText)
			.map(this::deleteImage)
			.toList();

		return CompletableFuture.allOf(deleteFutures.toArray(CompletableFuture[]::new))
			.thenRun(() -> log.info("[S3] 다중 파일 삭제 완료: {}개", deleteFutures.size()))
			.orTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
	}

	/**
	 * 단일 파일을 S3에 비동기적으로 업로드하는 내부 메서드입니다.
	 *
	 * @param file    업로드할 MultipartFile 객체
	 * @param dirName S3에 업로드될 디렉토리 이름
	 * @return 업로드된 파일의 S3 URL을 포함하는 CompletableFuture
	 */
	private CompletableFuture<String> uploadSingleFileInternal(MultipartFile file, String dirName) {
		return CompletableFuture.supplyAsync(() -> {
			ImageValidator.validate(file);
			return executeUpload(file, dirName);
		}, imageUploadExecutor);
	}

	/**
	 * 실제 파일 업로드 작업을 수행합니다.
	 *
	 * @param file    업로드할 MultipartFile 객체
	 * @param dirName S3에 업로드될 디렉토리 이름
	 * @return 업로드된 파일의 S3 URL 문자열
	 * @throws ImageException 파일 업로드 실패 시 발생
	 */
	private String executeUpload(MultipartFile file, String dirName) {
		try {
			String s3Key = createS3Key(file.getOriginalFilename(), dirName);

			S3Resource uploaded = s3Template.upload(bucketName, s3Key, file.getInputStream());
			return uploaded.getURL().toString();
		} catch (IOException e) {
			throw new ImageException(ErrorCode.IMAGE_UPLOAD_FAILED);
		}
	}

	/**
	 * 실제 파일 삭제 작업을 수행합니다.
	 *
	 * @param s3Url 삭제할 이미지의 S3 URL
	 * @throws ImageException URL이 유효하지 않거나 삭제 실패 시 발생
	 */
	private void executeDelete(String s3Url) {
		try {
			s3Template.deleteObject(bucketName, extractKeyFromUrl(s3Url));
		} catch (Exception e) {
			throw new ImageException(ErrorCode.IMAGE_DELETE_FAILED);
		}
	}

	/**
	 * S3에 저장될 객체의 키를 생성합니다.
	 *
	 * @param originalFilename 원본 파일명
	 * @param dirName          디렉토리 이름
	 * @return 생성된 S3 객체 키
	 */
	private String createS3Key(String originalFilename, String dirName) {
		String uniqueFileName = createUniqueFileName(originalFilename);
		return String.format("%s%s%s", dirName, PATH_SEPARATOR, uniqueFileName);
	}

	/**
	 * 고유한 파일명을 생성합니다.
	 *
	 * @param originalFilename 원본 파일명
	 * @return UUID를 포함한 고유한 파일명
	 */
	private String createUniqueFileName(String originalFilename) {
		String extension = StringUtils.getFilenameExtension(originalFilename);
		String uuid = UUID.randomUUID().toString();
		return uuid + "." + extension;
	}

	/**
	 * S3 URL에서 객체 키를 추출합니다.
	 *
	 * @param s3Url S3 객체의 URL
	 * @return S3 객체 키
	 * @throws ImageException URL이 유효하지 않을 경우 발생
	 */
	private String extractKeyFromUrl(String s3Url) {
		if (s3Url.contains(AWS_S3_DOMAIN)) {
			String objectKey = s3Url.substring(s3Url.indexOf(AWS_S3_DOMAIN) + AWS_S3_DOMAIN.length());
			log.info(objectKey);
			return objectKey;
		}
		throw new ImageException(ErrorCode.URL_NOT_VALID);
	}
}
