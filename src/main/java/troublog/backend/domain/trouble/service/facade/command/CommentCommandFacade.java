package troublog.backend.domain.trouble.service.facade.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.converter.CommentConverter;
import troublog.backend.domain.trouble.dto.request.CommentReqDto;
import troublog.backend.domain.trouble.dto.response.CommentResDto;
import troublog.backend.domain.trouble.entity.Comment;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.service.command.CommentCommandService;
import troublog.backend.domain.trouble.service.facade.relation.CommentRelationFacade;
import troublog.backend.domain.trouble.service.factory.CommentFactory;
import troublog.backend.domain.trouble.service.factory.PostFactory;
import troublog.backend.domain.trouble.service.query.CommentQueryService;
import troublog.backend.domain.trouble.service.query.PostQueryService;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentCommandFacade {

	private final PostQueryService postQueryService;
	private final CommentCommandService commentCommandService;
	private final CommentRelationFacade commentRelationFacade;
	private final CommentQueryService commentQueryService;

	public CommentResDto createComment(Long userId, Long postId, CommentReqDto commentReqDto) {
		Post post = postQueryService.findById(postId);
		PostFactory.validateVisibility(post);

		Comment newComment = CommentConverter.toEntity(commentReqDto);
		commentRelationFacade.establishRelations(newComment, userId, postId);
		Comment savedComment = commentCommandService.save(newComment);
		return CommentConverter.toResponse(savedComment);
	}

	public CommentResDto createChildComment(Long userId, CommentReqDto commentReqDto, Long commentId, Long postId) {
		Post post = postQueryService.findById(postId);
		PostFactory.validateVisibility(post);

		Comment parentComment = commentQueryService.findComment(commentId);
		CommentFactory.validateParent(parentComment);

		Comment newChildComment = CommentConverter.toEntity(commentReqDto);
		commentRelationFacade.establishChildRelations(parentComment, newChildComment);
		commentRelationFacade.establishRelations(newChildComment, userId, postId);
		Comment savedComment = commentCommandService.save(newChildComment);
		return CommentConverter.toChildResponse(savedComment);
	}

	public CommentResDto updateComment(Long userId, CommentReqDto commentReqDto, Long commentId, Long postId) {
		Post post = postQueryService.findById(postId);
		PostFactory.validateVisibility(post);
		Comment comment = commentQueryService.findComment(commentId);
		CommentFactory.validateAuthorized(userId, comment);

		comment.updateContent(commentReqDto.contents());
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
