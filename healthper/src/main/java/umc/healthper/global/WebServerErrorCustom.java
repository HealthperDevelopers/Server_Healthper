package umc.healthper.global;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class WebServerErrorCustom implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        ErrorPage error404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage error500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        ErrorPage error406 = new ErrorPage(HttpStatus.NOT_ACCEPTABLE, "/error-page/406");
        ErrorPage error405 = new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/error-page/405");
        factory.addErrorPages(error404, error405,error406,error500);
    }
}
