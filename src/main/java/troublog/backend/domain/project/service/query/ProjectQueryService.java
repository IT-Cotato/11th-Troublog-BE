package troublog.backend.domain.project.service.query;

import static org.springframework.data.domain.Sort.Direction.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import troublog.backend.domain.project.converter.ProjectConverter;
import troublog.backend.domain.project.dto.response.ProjectDetailResDto;
import troublog.backend.domain.project.entity.Project;
import troublog.backend.domain.project.repository.ProjectRepository;
import troublog.backend.domain.trouble.converter.ListConverter;
import troublog.backend.domain.trouble.dto.response.TroubleListResDto;
import troublog.backend.domain.trouble.entity.Post;
import troublog.backend.domain.trouble.enums.ContentSummaryType;
import troublog.backend.domain.trouble.enums.PostStatus;
import troublog.backend.domain.trouble.enums.SortType;
import troublog.backend.domain.trouble.enums.TagType;
import troublog.backend.domain.trouble.enums.VisibilityType;
import troublog.backend.domain.trouble.repository.PostRepository;
import troublog.backend.global.common.error.ErrorCode;
import troublog.backend.global.common.error.exception.ProjectException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectQueryService {

	private final ProjectRepository projectRepository;
	private final PostRepository postRepository;

	public Project findById(long id) {
		log.info("[Project] 프로젝트 조회: projectId={}", id);
		return projectRepository.findById(id)
			.orElseThrow(() -> new ProjectException(ErrorCode.PROJECT_NOT_FOUND));
	}

	public ProjectDetailResDto getDetails(Project project) {
		log.info("[Project] 상세조회: projectId={}, projectName={}", project.getId(), project.getName());

		List<String> topTags = projectRepository.findTop2TagsByProjectId(
			project.getId(), TagType.TECH_STACK, PageRequest.of(0, 2)
		);
		log.info("[Project] 태그 조회 결과: projectId={}, topTags={}", project.getId(), topTags);
		return ProjectConverter.toResponseDetail(project, topTags);
	}

	public List<ProjectDetailResDto> getAllProjects(Long userId) {
		List<Project> projects = projectRepository.findAllByUserIdAndIsDeletedFalse(userId);
		log.info("[Project] 전체 프로젝트 조회: userId={}, projectCount={}", userId, projects.size());
		return projects.stream()
			.map(this::getDetails)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<TroubleListResDto> getCompletedTroubles(
		Long projectId, SortType sort, VisibilityType visibility) {
		Sort s = sort == SortType.IMPORTANT
			? Sort.by(DESC, "starRating", "id")
			: Sort.by(DESC, "completedAt", "id");
		Boolean visible = mapVisibility(visibility);
		List<Post> posts = postRepository.findByProjectCompleted(projectId, PostStatus.COMPLETED, visible, s);
		if (posts.isEmpty())
			return List.of();

		return posts.stream()
			.map(ListConverter::toAllTroubleListResDto)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<TroubleListResDto> getSummarizedTroubles(
		Long projectId, SortType sort, ContentSummaryType summaryType) {
		Sort s = sort == SortType.IMPORTANT
			? Sort.by(DESC, "starRating", "id")
			: Sort.by(DESC, "completedAt", "id");
		ContentSummaryType st = (summaryType == ContentSummaryType.NONE) ? null : summaryType;
		List<Post> posts = postRepository.findByProjectSummarized(projectId, PostStatus.SUMMARIZED, st, s);

		if (posts.isEmpty())
			return List.of();

		return posts.stream()
			.map(ListConverter::toAllTroubleListResDto)
			.toList();
	}

	private Boolean mapVisibility(VisibilityType v) {
		if (v == null || v == VisibilityType.ALL)
			return null;
		return (v == VisibilityType.PUBLIC) ? Boolean.TRUE : Boolean.FALSE;
	}
}