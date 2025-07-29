package troublog.backend.domain.image.service.facade;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.image.service.s3.S3Uploader;

/**
 * 썸네일 이미지, 유저 프로필 이미지 저장 및 삭제을 위한 클래스
 */
@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageFacade {
	private final S3Uploader s3Uploader;

	public String saveImage(MultipartFile file, String dirName) {
		return s3Uploader.uploadSingleImage(file, dirName).join();
	}

	public void deleteImage(String imageUrl) {
		s3Uploader.deleteImage(imageUrl);
	}
}