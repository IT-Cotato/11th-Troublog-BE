package troublog.backend.domain.image.validator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.experimental.UtilityClass;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.ImageException;

@UtilityClass
public class ImageValidator {

	private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "gif"));
	private static final long MAX_FILE_SIZE = (long)10 * 1024 * 1024;
	private static final String IMAGE_CONTENT_TYPE = "image";

	public void validate(MultipartFile file) {
		validateEmptyFile(file);
		validateExtension(file);
		validateFileContentType(file);
		validateFileSize(file);
	}

	private void validateExtension(MultipartFile file) {
		String filename = file.getOriginalFilename();
		if (!StringUtils.hasText(filename)) {
			throw new ImageException(ErrorCode.IMAGE_UPLOAD_FAILED);
		}
		String extension = StringUtils.getFilenameExtension(filename.toLowerCase());
		if (!ALLOWED_EXTENSIONS.contains(extension)) {
			throw new ImageException(ErrorCode.IMAGE_UPLOAD_FAILED);
		}
	}

	private void validateFileContentType(MultipartFile file) {
		String contentType = file.getContentType();
		if (contentType == null || !contentType.startsWith(IMAGE_CONTENT_TYPE)) {
			throw new ImageException(ErrorCode.IMAGE_UPLOAD_FAILED);
		}
	}

	private void validateFileSize(MultipartFile file) {
		if (file.getSize() > MAX_FILE_SIZE) {
			throw new ImageException(ErrorCode.FILE_SIZE_EXCEEDING);
		}
	}

	private void validateEmptyFile(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new ImageException(ErrorCode.FILE_NOT_FOUND);
		}
	}

	public void validateS3Url(String s3Url) {
		if (!StringUtils.hasText(s3Url)) {
			throw new ImageException(ErrorCode.URL_NOT_VALID);
		}
	}

	public void validateFileList(List<MultipartFile> fileList) {
		if (fileList == null || fileList.isEmpty()) {
			throw new ImageException(ErrorCode.IMAGE_UPLOAD_FAILED);
		}

		fileList.forEach(ImageValidator::validate);
	}
}