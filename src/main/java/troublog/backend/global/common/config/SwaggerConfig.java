package troublog.backend.global.common.config;

import java.util.Collections;
import java.util.List;

import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

	private final Environment env;

	@Bean
	public OpenAPI customOpenAPI() {

		// 현재 활성화된 profile 가져오기
		String profile = env.getActiveProfiles().length > 0 ? env.getActiveProfiles()[0] : "local";

		// Security Scheme 정의
		SecurityScheme accessTokenAuth = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER)
			.name("Authorization");

		// // TODO : 스웨거에서 임시로 리프레시토큰을 헤더로 받음
		// SecurityScheme refreshTokenAuth = new SecurityScheme()
		//  .type(SecurityScheme.Type.APIKEY)
		//  .in(SecurityScheme.In.HEADER)
		//  .name("refreshToken");

		SecurityScheme securitySchemeEnvType = new SecurityScheme()
			.type(SecurityScheme.Type.APIKEY)
			.in(SecurityScheme.In.HEADER).name("EnvType");

		// Security Requirement 정의
		SecurityRequirement securityRequirement = new SecurityRequirement()
			.addList("accessTokenAuth")
			.addList("refreshTokenAuth")
			.addList("EnvType");

		Server server = new Server();
		switch (profile) {
			// TODO : 추후 적용
			// case "prod" -> {
			// 	server.setUrl("http://3.37.163.222:8080");
			// 	server.setDescription("운영 서버");
			// }
			case "dev" -> {
				server.setUrl("https://troublog.shop");
				server.setDescription("개발 서버");
			}
			default -> {
				server.setUrl("http://localhost:8080");
				server.setDescription("로컬 서버");
			}
		}

		return new OpenAPI()
			.info(new Info().title("Troublog API")
				.description("트러블로그 API 서버")
				.version("v1.0"))
			.components(new Components()
				.addSecuritySchemes("accessTokenAuth", accessTokenAuth)
				.addSecuritySchemes("EnvType", securitySchemeEnvType))
			.security(Collections.singletonList(securityRequirement))
			.servers(List.of(server));
	}

	@Bean
	@Primary
	public SwaggerUiConfigProperties swaggerUiConfigProperties(SwaggerUiConfigProperties props) {
		props.setPersistAuthorization(true);
		return props;
	}

}
