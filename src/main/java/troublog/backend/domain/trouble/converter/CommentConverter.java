package troublog.backend.domain.trouble.converter;

import java.util.List;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.dto.request.CommentReqDto;
import troublog.backend.domain.trouble.dto.response.CommentDetailResDto;
import troublog.backend.domain.trouble.dto.response.CommentResDto;
import troublog.backend.domain.trouble.entity.Comment;

@UtilityClass
public class CommentConverter {

	public Comment toEntity(CommentReqDto commentReqDto) {
		return Comment.builder()
			.content(commentReqDto.contents())
			.isDeleted(false)
			.build();
	}

	public CommentResDto toResponse(Comment comment) {
		return CommentResDto.builder()
			.commentId(comment.getId())
			.postId(comment.getPost().getId())
			.userId(comment.getUser().getId())
			.content(comment.getContent())
			.createdAt(comment.getCreatedAt())
			.build();
	}

	public CommentResDto toChildResponse(Comment comment) {
		return CommentResDto.builder()
			.commentId(comment.getId())
			.postId(comment.getPost().getId())
			.userId(comment.getUser().getId())
			.content(comment.getContent())
			.createdAt(comment.getCreatedAt())
			.parentCommentId(comment.getParentComment().getId())
			.build();
	}

	public CommentDetailResDto toDetailResponse(Comment comment) {
		List<CommentResDto> childDtoList = comment.getChildComments().stream()
			.map(CommentConverter::toResponse)
			.collect(Collectors.toList());

		return CommentDetailResDto.builder()
			.postId(comment.getId())
			.postId(comment.getPost().getId())
			.userId(comment.getUser().getId())
			.content(comment.getContent())
			.createdAt(comment.getCreatedAt())
			.commentList(childDtoList)
			.build();
	}

}
