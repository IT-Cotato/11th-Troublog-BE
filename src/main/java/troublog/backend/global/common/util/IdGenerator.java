package troublog.backend.global.common.util;

import java.util.UUID;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IdGenerator {
	public String generate() {
		return UUID.randomUUID().toString();
	}
}