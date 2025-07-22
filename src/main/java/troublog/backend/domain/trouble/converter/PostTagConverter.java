package troublog.backend.domain.trouble.converter;

import lombok.experimental.UtilityClass;
import troublog.backend.domain.trouble.entity.PostTag;

@UtilityClass
public class PostTagConverter {

	public String toTechStackList(PostTag postTag) {
		return postTag.getTechStack().getDescription();
	}
}
