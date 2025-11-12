package troublog.backend.domain.trouble.usecase;

import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.trouble.converter.PostConverter;
import troublog.backend.domain.trouble.dto.request.PostReqDto;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.service.command.PostCommandService;
import troublog.backend.domain.trouble.service.facade.relation.PostRelationFacade;
import troublog.backend.domain.trouble.service.factory.PostFactory;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCreateUseCase {
	private final PostFactory postFactory;
	private final PostRelationFacade relationFacade;
	private final PostCommandService postCommandService;

	public PostResDto createPost(final Long userId, final PostReqDto postReqDto) {
		log.info("[Post] 트러블슈팅 문서 생성 시작: userId = {}", userId);
		Post newPost = postFactory.createPostWithRequireRelations(postReqDto);
		relationFacade.establishRequireRelations(newPost, userId, postReqDto);
		Post savedPost = postCommandService.savePost(newPost);
		relationFacade.establishSecondaryRelations(savedPost, postReqDto);
		return PostConverter.toResponse(savedPost);
	}
}