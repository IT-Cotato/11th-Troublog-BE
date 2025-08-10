package troublog.backend.domain.trouble.service.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.converter.CommentConverter;
import troublog.backend.domain.trouble.dto.request.CommentReqDto;
import troublog.backend.domain.trouble.dto.request.CommentUpdateReqDto;
import troublog.backend.domain.trouble.dto.response.CommentResDto;
import troublog.backend.domain.trouble.entity.Comment;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.service.command.CommentCommandService;
import troublog.backend.domain.trouble.service.factory.CommentFactory;
import troublog.backend.domain.trouble.service.factory.PostFactory;
import troublog.backend.domain.trouble.service.query.CommentQueryService;
import troublog.backend.domain.trouble.service.query.PostQueryService;
import troublog.backend.domain.user.service.query.UserQueryService;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentCommandFacade {

	private final PostQueryService postQueryService;
	private final CommentCommandService commentCommandService;
	private final UserQueryService userQueryService;
	private final CommentRelationFacade commentRelationFacade;
	private final CommentQueryService commentQueryService;

	public CommentResDto createComment(Long userId, CommentReqDto commentReqDto) {
		Post post = postQueryService.findById(commentReqDto.postId());
		PostFactory.validateVisibility(post);

		Comment newComment = CommentConverter.toEntity(commentReqDto);
		commentRelationFacade.establishRelations(newComment, userId, commentReqDto);
		Comment savedComment = commentCommandService.save(newComment);
		return CommentConverter.toResponse(savedComment);
	}

	public CommentResDto createChildComment(Long userId, CommentReqDto commentReqDto, Long commentId) {
		Post post = postQueryService.findById(commentReqDto.postId());
		PostFactory.validateVisibility(post);

		Comment parentComment = commentQueryService.findComment(commentId);
		CommentFactory.validateParent(parentComment);

		Comment newChildComment = CommentConverter.toEntity(commentReqDto);
		commentRelationFacade.establishChildRelations(parentComment, newChildComment);
		commentRelationFacade.establishRelations(newChildComment, userId, commentReqDto);
		Comment savedComment = commentCommandService.save(newChildComment);
		return CommentConverter.toChildResponse(savedComment);
	}

	public CommentResDto updateComment(Long userId, CommentUpdateReqDto commentUpdateReqDto) {
		Comment comment = commentQueryService.findComment(commentUpdateReqDto.id());
		CommentFactory.validateAuthorized(userId, comment);

		comment.updateContent(commentUpdateReqDto.contents());
		return CommentConverter.toResponse(comment);
	}

	public void hardDeleteComment(Long userId, long commentId) {
		Comment comment = commentQueryService.findComment(commentId);
		CommentFactory.validateAuthorized(userId, comment);
		commentCommandService.delete(comment);
	}

	public void softDeleteComment(Long userId, long commentId) {
		Comment comment = commentQueryService.findComment(commentId);
		CommentFactory.validateAuthorized(userId, comment);
		comment.markAsDeleted();
	}
}
