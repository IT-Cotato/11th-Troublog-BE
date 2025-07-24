package troublog.backend.domain.trouble.service.factory;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.converter.PostConverter;
import troublog.backend.domain.trouble.dto.request.PostCreateReqDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.PostStatus;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostFactory {

	public Post createPost(PostCreateReqDto requestDto) {
		PostStatus status = PostStatus.from(requestDto.postStatus());
		return switch (status) {
			case WRITING -> PostConverter.createWritingPost(requestDto);
			case COMPLETED -> PostConverter.createCompletedPost(requestDto);
			case SUMMARIZED -> PostConverter.createSummarizedPost(requestDto);
		};
	}
}