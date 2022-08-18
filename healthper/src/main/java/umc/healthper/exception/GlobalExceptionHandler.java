package umc.healthper.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import umc.healthper.exception.post.PostAlreadyRemovedException;
import umc.healthper.exception.post.PostNotFoundException;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse postNotFoundExceptionHandle(PostNotFoundException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.NOT_FOUND,
                getMessage("postNotFound.code"),
                getMessage("postNotFound.message")
        );
    }

    @ExceptionHandler(PostAlreadyRemovedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse postAlreadyRemovedExceptionHandle(PostAlreadyRemovedException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.NOT_FOUND,
                getMessage("postAlreadyRemoved.code"),
                getMessage("postAlreadyRemoved.message")
        );
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, null);
    }
}
