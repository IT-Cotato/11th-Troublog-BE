package troublog.backend.domain.trouble.converter;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.entity.Post;

@UtilityClass
public class PostConverter {

	public Post toEntity() {
		return Post.builder().build();
	}

	public PostResDto toResponse() {
		return PostResDto.builder().build();
	}
}
