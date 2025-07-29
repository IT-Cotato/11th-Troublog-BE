package troublog.backend.domain.image.service.facade;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.image.converter.PostImageConverter;
import troublog.backend.domain.image.entity.PostImage;
import troublog.backend.domain.image.service.command.PostImageCommandService;
import troublog.backend.domain.image.service.query.PostImageQueryService;
import troublog.backend.domain.image.service.s3.S3Uploader;
import troublog.backend.domain.trouble.entity.Post;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImageFacade {
	public static final String POST_DIR = "post/";

	private final S3Uploader s3Uploader;
	private final PostImageCommandService postImageCommandService;
	private final PostImageQueryService postImageQueryService;

	public PostImage savePostImage(Post post, MultipartFile file) {
		return uploadAndSaveImage(file, false, post.getId());
	}

	public PostImage savePostImageWithThumbnail(Post post, MultipartFile file) {
		return uploadAndSaveImage(file, true, post.getId());
	}

	public List<PostImage> savePostImages(Long postId, List<MultipartFile> files) {
		return s3Uploader.uploadMultipleImages(files, POST_DIR + postId)
			.thenApply(imageUrls -> {
				List<PostImage> postImages = PostImageConverter.toEntityList(imageUrls);
				return postImageCommandService.saveAll(postImages);
			})
			.join();
	}

	public void deletePostImage(Long postImageId) {
		PostImage postImage = postImageQueryService.findById(postImageId);

		s3Uploader.deleteImage(postImage.getImageUrl())
			.thenRun(() -> postImageCommandService.delete(postImage))
			.join();
	}

	public void deletePostImages(Long postId) {
		List<PostImage> postImages = postImageQueryService.findAllByPostId(postId);

		if (postImages.isEmpty()) {
			return;
		}

		s3Uploader.deleteMultipleImages(PostImageConverter.toImageUrlList(postImages))
			.thenRun(() -> postImageCommandService.deleteAll(postImages))
			.join();
	}

	private PostImage uploadAndSaveImage(MultipartFile file, boolean isThumbnail, Long postId) {
		return s3Uploader.uploadSingleImage(file, POST_DIR + postId)
			.thenApply(imageUrl -> {
				PostImage postImage = PostImageConverter.toEntity(imageUrl, isThumbnail);
				return postImageCommandService.save(postImage);
			})
			.join();
	}
}