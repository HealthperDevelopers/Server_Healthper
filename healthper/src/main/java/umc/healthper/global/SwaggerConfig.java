package umc.healthper.global;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi healthperApi() {
        return GroupedOpenApi.builder()
                .group("healthper")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI healthperOpenApiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Healthper API")
                        .description("Healthper(헬퍼) Server API 명세서")
                        .version("v0.0.1"));
    }
}
