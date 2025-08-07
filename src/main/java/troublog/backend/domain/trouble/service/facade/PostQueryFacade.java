package troublog.backend.domain.trouble.service.facade;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.trouble.converter.ContentConverter;
import troublog.backend.domain.trouble.converter.PostConverter;
import troublog.backend.domain.trouble.dto.response.PostResDto;
import troublog.backend.domain.trouble.dto.response.common.ContentInfoDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.entity.PostTag;
import troublog.backend.domain.trouble.entity.Tag;
import troublog.backend.domain.trouble.enums.TagCategory;
import troublog.backend.domain.trouble.enums.TagType;
import troublog.backend.domain.trouble.service.factory.PostFactory;
import troublog.backend.domain.trouble.service.query.PostQueryService;
import troublog.backend.domain.trouble.service.query.TagQueryService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostQueryFacade {

	private final PostQueryService postQueryService;
	private final TagQueryService tagQueryService;

	public static String findErrorTag(Post post) {
		if (post.getPostTags() == null || post.getPostTags().isEmpty()) {
			return null;
		}
		return post.getPostTags().stream()
			.map(PostTag::getTag)
			.filter(tag -> tag != null && tag.getTagType() == TagType.ERROR)
			.map(Tag::getName)
			.findFirst()
			.orElse(null);
	}

	public static List<String> findTechStackTags(Post post) {
		if (post.getPostTags() == null || post.getPostTags().isEmpty()) {
			return List.of();
		}
		return post.getPostTags().stream()
			.map(PostTag::getTag)
			.filter(tag -> tag != null && tag.getTagType() == TagType.TECH_STACK)
			.map(Tag::getName)
			.toList();
	}

	public static List<ContentInfoDto> findContents(Post post) {
		if (post.getContents() == null || post.getContents().isEmpty()) {
			return List.of();
		}
		return post.getContents().stream()
			.map(ContentConverter::toResponse)
			.toList();
	}

	public PostResDto findPostDetailsById(Long userId, Long id) {
		//TODO N+1 Query 해결 필요
		Post post = postQueryService.findById(id);
		PostFactory.validateAuthorized(userId, post);
		return PostConverter.toResponse(post);
	}

	public List<PostResDto> findAllNotDeletedPosts() {
		List<Post> posts = postQueryService.findAllNotDeletedPosts();
		return PostConverter.toResponseList(posts);
	}

	public List<PostResDto> findDeletedPosts() {
		List<Post> posts = postQueryService.findAllDeletedPosts();
		return PostConverter.toResponseList(posts);
	}

	public List<String> findPostTagsByCategory(String category) {
		TagCategory tagCategory = TagCategory.from(category);
		List<Tag> techStacks = tagQueryService.findTechStackTagsByCategory(tagCategory);
		return techStacks.stream()
			.map(Tag::getName)
			.toList();
	}

	public List<String> findPostTagsByName(String name) {
		List<Tag> techStacks = tagQueryService.findTechStackTagContainsName(name);
		return techStacks.stream()
			.map(Tag::getName)
			.toList();
	}

	public Page<PostResDto> searchUserPostByKeyword(Long userId, String keyword, Pageable pageable) {
		Page<Post> posts = postQueryService.searchUserPostByKeyword(userId, keyword, pageable);
		return posts.map(PostConverter::toResponse);
	}

	public Page<PostResDto> searchPostByKeyword(String keyword, Pageable pageable) {
		Page<Post> posts = postQueryService.searchPostByKeyword(keyword, pageable);
		return posts.map(PostConverter::toResponse);
	}

	public PageRequest getPageable(int page, int size) {
		return PageRequest.of(Math.max(0, page - 1), size);
	}

	public Post findPostById(String id) {
		return postQueryService.findById(Long.parseLong(id));
	}
}
