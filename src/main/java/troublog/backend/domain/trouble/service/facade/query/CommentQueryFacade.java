package troublog.backend.domain.trouble.service.facade.query;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentQueryFacade {

	private final PostQueryService postQueryService;
	private final CommentQueryService commentQueryService;

	public List<CommentResDto> getComments(long postId) {
		Post post = postQueryService.findById(postId);
		List<Comment> comments = commentQueryService.findAllComment(post.getId());

		if (comments.isEmpty()) {
			return Collections.emptyList();
		}

		return comments.stream()
			.map(CommentConverter::toResponse)
			.collect(Collectors.toList());
	}

	public CommentDetailResDto getDetailComment(long commentId) {
		Comment comment = commentQueryService.findComment(commentId);
		return CommentConverter.toDetailResponse(comment);
	}
}
