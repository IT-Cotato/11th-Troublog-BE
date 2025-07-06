package troublog.backend.global.common.config;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
	@Bean
	public OpenAPI customOpenAPI() {

		// Security Scheme 정의
	 	SecurityScheme accessTokenAuth = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER)
			.name("Authorization");

		 // TODO : 스웨거에서 임시로 리프레시토큰을 헤더로 받음
		 SecurityScheme refreshTokenAuth = new SecurityScheme()
			 .type(SecurityScheme.Type.APIKEY)
			 .in(SecurityScheme.In.HEADER)
			 .name("refreshToken");

		 SecurityScheme securitySchemeEnvType = new SecurityScheme()
			 .type(SecurityScheme.Type.APIKEY)
			 .in(SecurityScheme.In.HEADER).name("EnvType");

		 // Security Requirement 정의
		SecurityRequirement securityRequirement = new SecurityRequirement()
			.addList("accessTokenAuth")
			.addList("refreshTokenAuth")
			.addList("EnvType");

		Server localServer = new Server();
		localServer.setUrl("http://localhost:8080");
		localServer.setDescription("로컬 서버");

		return new OpenAPI()
			.info(new Info().title("Troublog API")
				.description("트러블로그 API 서버")
				.version("v1.0"))
			.components(new Components()
				.addSecuritySchemes("accessTokenAuth", accessTokenAuth)
				.addSecuritySchemes("refreshTokenAuth", refreshTokenAuth)
				.addSecuritySchemes("EnvType", securitySchemeEnvType))
			.security(Collections.singletonList(securityRequirement))
			.servers(List.of(localServer));
	}
}
