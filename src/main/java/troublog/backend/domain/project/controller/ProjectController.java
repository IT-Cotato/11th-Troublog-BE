package troublog.backend.domain.project.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import troublog.backend.domain.project.dto.request.ProjectReqDto;
import troublog.backend.domain.project.dto.response.ProjectDetailResDto;
import troublog.backend.domain.project.dto.response.ProjectResDto;
import troublog.backend.domain.project.service.facade.ProjectCommandFacade;
import troublog.backend.domain.project.service.facade.ProjectQueryFacade;
import troublog.backend.domain.project.service.query.ProjectQueryService;
import troublog.backend.global.common.annotation.Authentication;
import troublog.backend.global.common.custom.CustomAuthenticationToken;
import troublog.backend.global.common.response.BaseResponse;
import troublog.backend.global.common.util.ResponseUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
@Tag(name = "프로젝트", description = "프로젝트 관련 API")
public class ProjectController {

	private final ProjectCommandFacade projectCommandFacade;
	private final ProjectQueryFacade projectQueryFacade;
	private final ProjectQueryService projectQueryService;

	// 프로젝트 생성
	@PostMapping("/create")
	@Operation(summary = "프로젝트 생성 API", description = "프로젝트를 생성합니다.")
	public ResponseEntity<BaseResponse<ProjectResDto>> createProject(
		@Authentication CustomAuthenticationToken auth,
		@Valid @RequestBody ProjectReqDto reqDto) {
		ProjectResDto response = projectCommandFacade.createProject(auth.getUserId(), reqDto);
		return ResponseUtils.created(response);
	}

	// 프로젝트 수정
	@PutMapping("/{projectId}")
	@Operation(summary = "프로젝트 수정 API", description = "프로젝트를 수정합니다.")
	public ResponseEntity<BaseResponse<ProjectResDto>> updateProject(
		@Authentication CustomAuthenticationToken auth,
		@PathVariable long projectId,
		@Valid @RequestBody ProjectReqDto reqDto) {
		ProjectResDto response = projectCommandFacade.updateProject(auth.getUserId(), reqDto, projectId);
		return ResponseUtils.ok(response);
	}

	// 프로젝트 삭제
	@DeleteMapping("/{projectId}")
	@Operation(summary = "프로젝트 삭제 API", description = "프로젝트를 삭제합니다.")
	public ResponseEntity<BaseResponse<Void>> deletePost(
		@Authentication CustomAuthenticationToken auth,
		@PathVariable long projectId) {
		projectCommandFacade.softDeleteProject(auth.getUserId(), projectId);
		return ResponseUtils.noContent();
	}

	// 프로젝트 상세 조회
	@GetMapping("/{projectId}")
	@Operation(summary = "프로젝트 상세 조회 API", description = "프로젝트를 하나를 조회합니다.")
	public ResponseEntity<BaseResponse<ProjectDetailResDto>> getProjectDetails(
		@Authentication CustomAuthenticationToken auth,
		@PathVariable long projectId) {
		ProjectDetailResDto response = projectQueryFacade.getDetailsProject(auth.getUserId(), projectId);
		return ResponseUtils.ok(response);
	}

	// 프로젝트 목록 조회
	@GetMapping("/list")
	@Operation(summary = "프로젝트 전체 목록 조회 API", description = "전체 프로젝트 리스트를 조회합니다.")
	public ResponseEntity<BaseResponse<List<ProjectDetailResDto>>> getProjects(
		@Authentication CustomAuthenticationToken auth) {
		List<ProjectDetailResDto> response = projectQueryService.getAllProjects(auth.getUserId());
		return ResponseUtils.ok(response);
	}

	// 프로젝트 썸네일 업로드

	// 트러블슈팅 목록 조회
	
}
