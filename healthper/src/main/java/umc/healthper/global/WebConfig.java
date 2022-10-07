package umc.healthper.global;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import umc.healthper.global.argumentresolver.LoginMemberArgResolver;
import umc.healthper.global.login.LoginCheckInterceptor;
import umc.healthper.service.MemberService;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final MemberService memberService;
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgResolver(memberService));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/home", "/member/add", "/signup", "/login", "/logout",
                        "/css/**", "/*.ico", "/error", "/error-page/**",
                        "/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**");
    }
//    @Bean
//    DispatcherServlet dispatcherServlet () {
//        DispatcherServlet ds = new DispatcherServlet();
//        ds.setThrowExceptionIfNoHandlerFound(true);
//        return ds;
//    }
}

