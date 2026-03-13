package troublog.backend.domain.trouble.service.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.alert.converter.AlertConverter;
import troublog.backend.domain.alert.dto.response.AlertResDto;
import troublog.backend.domain.alert.entity.Alert;
import troublog.backend.domain.alert.service.AlertCommandService;
import troublog.backend.domain.trouble.converter.CommentConverter;
import troublog.backend.domain.trouble.dto.request.CommentReqDto;
import troublog.backend.domain.trouble.dto.response.CommentResDto;
import troublog.backend.domain.trouble.entity.Comment;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.service.command.CommentCommandService;
import troublog.backend.domain.trouble.service.factory.CommentFactory;
import troublog.backend.domain.trouble.service.query.CommentQueryService;
import troublog.backend.domain.trouble.service.query.PostQueryService;
import troublog.backend.domain.trouble.validator.PostValidator;
import troublog.backend.domain.user.entity.User;
import troublog.backend.domain.user.service.query.UserQueryService;
import troublog.backend.global.common.constant.EnvType;
import troublog.backend.global.common.constant.FrontDomain;
import troublog.backend.global.common.util.AlertSseUtil;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentCommandFacadeService {

	private static final String COMMUNITY_REDIRECT_URL = "/user/community/";
	private final PostQueryService postQueryService;
	private final CommentCommandService commentCommandService;
	private final CommentRelationFacadeService commentRelationFacadeService;
	private final CommentQueryService commentQueryService;
	private final UserQueryService userQueryService;
	private final AlertCommandService alertCommandService;
	private final AlertSseUtil alertSseUtil;

	public CommentResDto createComment(Long userId, Long postId, CommentReqDto commentReqDto, String clientEnvType) {
		Post post = postQueryService.findById(postId);
		PostValidator.validateVisibility(post);
		User user = userQueryService.findUserById(userId);

		Comment newComment = CommentConverter.toEntity(commentReqDto);
		commentRelationFacadeService.establishRelations(newComment, user, post);
		Comment savedComment = commentCommandService.save(newComment);

		// 알림 전송
		String targetUrl =
			FrontDomain.fromEnvType(EnvType.valueOfEnvType(clientEnvType)) + COMMUNITY_REDIRECT_URL + post.getId();

		Alert alert = AlertConverter.postCommentAlert(post.getUser(), user.getNickname(), targetUrl);
		AlertResDto alertResDto = AlertConverter.convertToAlertResDto(alert);

		if (alertSseUtil.sendAlert(post.getUser().getId(), alertResDto)) {
			alert.markAsSent();
		}

		alertCommandService.save(alert);

		return CommentConverter.toResponse(savedComment);
	}

	public CommentResDto createChildComment(
		Long userId,
		CommentReqDto commentReqDto,
		Long commentId,
		Long postId,
		String clientEnvType
	) {
		Post post = postQueryService.findById(postId);
		PostValidator.validateVisibility(post);
		User user = userQueryService.findUserById(userId);

		Comment parentComment = commentQueryService.findComment(commentId);
		CommentFactory.validateParent(parentComment);
		PostValidator.validateCommentBelongsToPost(parentComment, postId);

		Comment newChildComment = CommentConverter.toEntity(commentReqDto);
		commentRelationFacadeService.establishChildRelations(parentComment, newChildComment);
		commentRelationFacadeService.establishRelations(newChildComment, user, post);
		Comment savedComment = commentCommandService.save(newChildComment);

		// 알림 전송
		String targetUrl =
			FrontDomain.fromEnvType(EnvType.valueOfEnvType(clientEnvType)) + COMMUNITY_REDIRECT_URL + post.getId();

		Alert alert = AlertConverter.postChildCommentAlert(parentComment.getUser(), user.getNickname(), targetUrl);
		AlertResDto alertResDto = AlertConverter.convertToAlertResDto(alert);

		if (alertSseUtil.sendAlert(parentComment.getUser().getId(), alertResDto)) {
			alert.markAsSent();
		}

		alertCommandService.save(alert);

		return CommentConverter.toChildResponse(savedComment);
	}

	public CommentResDto updateComment(Long userId, CommentReqDto commentReqDto, Long commentId, Long postId) {
		Post post = postQueryService.findById(postId);
		PostValidator.validateVisibility(post);
		Comment comment = commentQueryService.findComment(commentId);
		CommentFactory.validateAuthorized(userId, comment);
		PostValidator.validateCommentBelongsToPost(comment, postId);

		comment.updateContent(commentReqDto.contents());
		return CommentConverter.toResponse(comment);
	}

	public void hardDeleteComment(Long userId, long commentId) {
		Comment comment = commentQueryService.findComment(commentId);
		CommentFactory.validateAuthorized(userId, comment);
		commentCommandService.hardDelete(comment);
	}

	public void deleteComment(Long userId, long commentId) {
		Comment comment = commentQueryService.findComment(commentId);
		CommentFactory.validateAuthorized(userId, comment);
		// 대댓글이 있으면 softdelete
		if (commentQueryService.hasChildComments(commentId)) {
			commentCommandService.softDelete(comment);
			return;
		}
		// 대댓글이 없으면 harddelete
		commentCommandService.hardDelete(comment);
	}
}
