package troublog.backend.domain.image.service.factory;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.ImageException;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageFactory {

	private static final long MAX_FILE_SIZE = (long) 10 * 1024 * 1024; // 10MB
	private static final String IMAGE_CONTENT_TYPE = "image/";
	private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");

	public static void validateFile(MultipartFile file) {
		validateEmptyFile(file);
		validateFileSize(file);
		validateFileContentType(file);
		validateExtension(file);
	}

	private static void validateExtension(MultipartFile file) {
		String filename = file.getOriginalFilename();
		if (filename != null) {
			String extension = StringUtils.getFilenameExtension(filename.toLowerCase());
			if (!ALLOWED_EXTENSIONS.contains(extension)) {
				throw new ImageException(ErrorCode.IMAGE_UPLOAD_FAILED);
			}
		}
	}

	private static void validateFileContentType(MultipartFile file) {
		String contentType = file.getContentType();
		if (contentType == null || !contentType.startsWith(IMAGE_CONTENT_TYPE)) {
			throw new ImageException(ErrorCode.IMAGE_UPLOAD_FAILED);
		}
	}

	private static void validateFileSize(MultipartFile file) {
		if (file.getSize() > MAX_FILE_SIZE) {
			throw new ImageException(ErrorCode.FILE_SIZE_EXCEEDING);
		}
	}

	private static void validateEmptyFile(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new ImageException(ErrorCode.FILE_NOT_FOUND);
		}
	}

	public static void validateS3Url(String s3Url) {
		if (!StringUtils.hasText(s3Url)) {
			throw new ImageException(ErrorCode.URL_NOT_VALID);
		}
	}

	public static void validateFileList(List<MultipartFile> fileList) {
		if (fileList == null || fileList.isEmpty()) {
			throw new ImageException(ErrorCode.IMAGE_UPLOAD_FAILED);
		}

		fileList.forEach(ImageFactory::validateFile);
	}
}
