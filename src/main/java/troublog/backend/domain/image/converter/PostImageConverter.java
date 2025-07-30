package troublog.backend.domain.image.converter;

import java.util.List;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.image.entity.PostImage;

@UtilityClass
public class PostImageConverter {
	public PostImage toEntity(String imageUrl, Boolean isThumbnail) {
		return PostImage.builder()
			.imageUrl(imageUrl)
			.isThumbnail(isThumbnail)
			.build();
	}

	public List<PostImage> toEntityList(List<String> imageUrls) {
		return imageUrls.stream()
			.map(url -> toEntity(url, false))
			.toList();
	}

	public List<String> toImageUrlList(List<PostImage> postImages) {
		return postImages.stream()
			.map(PostImage::getImageUrl)
			.toList();
	}

}
