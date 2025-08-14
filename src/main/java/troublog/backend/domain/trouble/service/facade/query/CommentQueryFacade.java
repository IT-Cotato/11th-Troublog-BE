package troublog.backend.domain.trouble.service.facade.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.converter.CommentConverter;
import troublog.backend.domain.trouble.dto.response.CommentDetailResDto;
import troublog.backend.domain.trouble.dto.response.CommentResDto;
import troublog.backend.domain.trouble.entity.Comment;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.service.query.CommentQueryService;
import troublog.backend.domain.trouble.service.query.PostQueryService;
import troublog.backend.domain.trouble.validator.PostValidator;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentQueryFacade {

	private final PostQueryService postQueryService;
	private final CommentQueryService commentQueryService;

	public Page<CommentResDto> getComments(long postId, Pageable pageable) {
		Post post = postQueryService.findById(postId);
		Page<Comment> comments = commentQueryService.findAllComment(post.getId(), pageable);
		return comments.map(CommentConverter::toResponse);
	}

	public CommentDetailResDto getDetailComment(long commentId, Long postId) {
		Comment comment = commentQueryService.findComment(commentId);
		PostValidator.validateCommentBelongsToPost(comment, postId);
		return CommentConverter.toDetailResponse(comment);
	}
}
