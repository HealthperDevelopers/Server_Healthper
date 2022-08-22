package umc.healthper.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HttpStatusErrorHandler {

    private final MessageSource messageSource;

    @RequestMapping("/error-page/404")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse badURL(){
        log.error("404 error");
        return new ExceptionResponse(
                HttpStatus.NOT_FOUND,
                getMessage("wrongURL.code"),
                getMessage("wrongURL.message")
        );
    }

    @RequestMapping("/error-page/405")
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ExceptionResponse checkMethod(){
        log.error("405 error");
        return new ExceptionResponse(
                HttpStatus.NOT_ACCEPTABLE,
                getMessage("methodError.code"),
                getMessage("methodError.message")
        );
    }

    @RequestMapping("/error-page/406")
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ExceptionResponse unAuth(){
        log.error("406 error");
        return new ExceptionResponse(
                HttpStatus.NOT_ACCEPTABLE,
                getMessage("illegalAccess.code"),
                getMessage("illegalAccess.message")
        );
    }

    @RequestMapping("/error-page/500")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse badServer(){
        log.error("500 error");
        return new ExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                getMessage("serverError.code"),
                getMessage("serverError.message")
        );
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, null);
    }
}
