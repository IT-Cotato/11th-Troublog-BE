package troublog.backend.domain.trouble.converter;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.dto.response.TroubleResDto;
import troublog.backend.domain.trouble.entity.Trouble;

@UtilityClass
public class TroubleConverter {

	public Trouble toEntity() {
		return Trouble.builder().build();
	}

	public TroubleResDto toResponse() {
		return TroubleResDto.builder().build();
	}
}
