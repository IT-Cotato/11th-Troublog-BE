package troublog.backend.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import troublog.backend.domain.user.entity.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FollowDto(

	@Schema(description = "팔로워")
		@JsonProperty("follower")
	User follower,

	@Schema(description = "팔로잉")
		@JsonProperty("following")
	User following
) {}
