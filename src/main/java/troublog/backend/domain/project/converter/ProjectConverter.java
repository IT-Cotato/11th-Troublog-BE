package troublog.backend.domain.project.converter;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.project.dto.request.ProjectReqDto;
import troublog.backend.domain.project.dto.response.ProjectResDto;
import troublog.backend.domain.project.entity.Project;

@UtilityClass
public class ProjectConverter {

	public Project toEntity(ProjectReqDto reqDto) {
		return Project.builder()
			.name(reqDto.name())
			.description(reqDto.description())
			.thumbnailImageUrl(reqDto.thumbnailImageUrl())
			.build();
	}

	public ProjectResDto toResponse(Project project) {
		return ProjectResDto.builder()
			.id(project.getId())
			.name(project.getName())
			.description(project.getDescription())
			.thumbnailImageUrl(project.getThumbnailImageUrl())
			.build();
	}
}