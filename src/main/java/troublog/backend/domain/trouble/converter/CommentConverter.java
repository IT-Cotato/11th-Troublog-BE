package troublog.backend.domain.trouble.converter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
		Long parentId = (comment.getParentComment() != null) ? comment.getParentComment().getId() : null;

		return CommentResDto.builder()
			.commentId(comment.getId())
			.postId(comment.getPost().getId())
			.userId(comment.getUser().getId())
			.name(comment.getUser().getNickname())
			.profileImg(comment.getUser().getProfileUrl())
			.content(comment.getContent())
			.createdAt(comment.getCreatedAt())
			.parentCommentId(parentId)
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
		List<CommentResDto> childDtoList = Optional.ofNullable(comment.getChildComments())
			.orElse(Collections.emptyList())
			.stream()
			.map(CommentConverter::toResponse)
			.collect(Collectors.toList());

		return CommentDetailResDto.builder()
			.commentId(comment.getId())
			.postId(comment.getPost().getId())
			.userId(comment.getUser().getId())
			.content(comment.getContent())
			.createdAt(comment.getCreatedAt())
			.commentList(childDtoList)
			.build();
	}

}
