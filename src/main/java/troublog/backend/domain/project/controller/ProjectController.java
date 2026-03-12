package troublog.backend.domain.project.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.project.dto.request.ProjectReqDto;
import troublog.backend.domain.project.dto.response.ProjectDetailResDto;
import troublog.backend.domain.project.dto.response.ProjectResDto;
import troublog.backend.domain.project.service.facade.ProjectCommandFacadeService;
import troublog.backend.domain.project.service.facade.ProjectQueryFacadeService;
import troublog.backend.domain.trouble.dto.response.TroubleListResDto;
import troublog.backend.domain.trouble.enums.PostViewFilter;
import troublog.backend.domain.trouble.enums.SortType;
import troublog.backend.domain.trouble.enums.SummaryType;
import troublog.backend.domain.trouble.enums.VisibilityType;
import troublog.backend.global.common.annotation.Authentication;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.response.PageResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
@Tag(name = "프로젝트", description = "프로젝트 관련 API")
public class ProjectController {

	private final ProjectCommandFacadeService projectCommandFacadeService;
	private final ProjectQueryFacadeService projectQueryFacadeService;

	@PostMapping
	@Operation(summary = "프로젝트 생성 API", description = "프로젝트를 생성합니다.")
	public ResponseEntity<BaseResponse<ProjectResDto>> createProject(
		@Authentication CustomAuthenticationToken auth,
		@Valid @RequestBody ProjectReqDto reqDto
	) {
		ProjectResDto response = projectCommandFacadeService.createProject(auth.getUserId(), reqDto);
		return ResponseUtils.created(response);
	}

	@PutMapping("/{projectId}")
	@Operation(summary = "프로젝트 수정 API", description = "프로젝트를 수정합니다.")
	public ResponseEntity<BaseResponse<ProjectResDto>> updateProject(
		@Authentication CustomAuthenticationToken auth,
		@PathVariable long projectId,
		@Valid @RequestBody ProjectReqDto reqDto
	) {
		ProjectResDto response = projectCommandFacadeService.updateProject(auth.getUserId(), reqDto, projectId);
		return ResponseUtils.ok(response);
	}

	@DeleteMapping("/{projectId}")
	@Operation(summary = "프로젝트 soft delete API", description = "프로젝트를 삭제합니다.")
	public ResponseEntity<BaseResponse<Void>> deleteProject(
		@Authentication CustomAuthenticationToken auth,
		@PathVariable long projectId
	) {
		projectCommandFacadeService.softDeleteProject(auth.getUserId(), projectId);
		return ResponseUtils.noContent();
	}

	@GetMapping("/{projectId}")
	@Operation(summary = "프로젝트 상세 조회 API", description = "프로젝트 하나를 조회합니다.")
	public ResponseEntity<BaseResponse<ProjectDetailResDto>> getProjectDetails(
		@Authentication CustomAuthenticationToken auth,
		@PathVariable long projectId
	) {
		ProjectDetailResDto response = projectQueryFacadeService.getDetailsProject(auth.getUserId(), projectId);
		return ResponseUtils.ok(response);
	}

	@GetMapping
	@Operation(summary = "프로젝트 전체 목록 조회 API", description = "전체 프로젝트 리스트를 조회합니다. (삭제된 프로젝트는 조회되지 않음)")
	public ResponseEntity<PageResponse<ProjectDetailResDto>> getProjects(
		@Authentication CustomAuthenticationToken auth,
		@RequestParam(defaultValue = "1") @Min(1) int page,
		@RequestParam(defaultValue = "10") @Min(1) int size
	) {
		Pageable pageable = projectQueryFacadeService.getPageable(page, size);
		Page<ProjectDetailResDto> response = projectQueryFacadeService.getAllProjects(auth.getUserId(), pageable);
		return ResponseUtils.page(response);
	}

	@GetMapping("/{projectId}/troubles")
	@Operation(summary = "프로젝트 내 트러블슈팅 목록 조회 API", description =
		"작성 중(WRITING)/작성 완료(COMPLETED) 상태를 필터링해 조회합니다. "
			+ "작성 중은 WRITING 상태만 조회하고, 작성 완료는 COMPLETED와 SUMMARIZED 상태를 모두 조회합니다. "
			+ "visibility 파라미터로 공개/비공개 필터링이 가능합니다.")
	public ResponseEntity<BaseResponse<List<TroubleListResDto>>> getProjectTroubles(
		@Authentication CustomAuthenticationToken auth,
		@PathVariable Long projectId,
		@RequestParam PostViewFilter status,
		@RequestParam(defaultValue = "LATEST") SortType sort,
		@RequestParam(defaultValue = "ALL", required = false) VisibilityType visibility
	) {
		List<TroubleListResDto> response = projectQueryFacadeService.getProjectTroubles(
			auth.getUserId(),
			projectId,
			status,
			sort,
			visibility
		);
		return ResponseUtils.ok(response);
	}

	@GetMapping("/{projectId}/summaries")
	@Operation(summary = "프로젝트 내 요약본 목록 조회 API", description = "프로젝트내 존재하는 요약본을 요약 타입을 필터링해 조회합니다.")
	public ResponseEntity<BaseResponse<List<TroubleListResDto>>> getProjectSummaries(
		@Authentication CustomAuthenticationToken auth,
		@PathVariable Long projectId,
		@RequestParam(defaultValue = "LATEST") SortType sort,
		@RequestParam(defaultValue = "NONE") SummaryType summaryType
	) {
		List<TroubleListResDto> response = projectQueryFacadeService.getProjectTroubleSummaries(
			auth.getUserId(),
			projectId,
			sort,
			summaryType
		);
		return ResponseUtils.ok(response);
	}
}
