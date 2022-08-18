package umc.healthper.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import umc.healthper.exception.member.MemberDuplicateException;
import umc.healthper.exception.member.MemberNotFoundByIdException;
import umc.healthper.exception.member.MemberNotFoundByKakaoKeyException;
import umc.healthper.exception.post.PostAlreadyRemovedException;
import umc.healthper.exception.post.PostNotFoundException;
import umc.healthper.exception.postlike.AlreadyPostLikeException;
import umc.healthper.exception.postlike.NeverPostLikeException;
import umc.healthper.exception.postlike.PostLikeNotFoundException;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Member
     */
    @ExceptionHandler({MemberNotFoundByIdException.class, MemberNotFoundByKakaoKeyException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse memberNotFoundExceptionHandle(Exception e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.NOT_FOUND,
                getMessage("memberNotFound.code"),
                getMessage("memberNotFound.message")
        );
    }

    @ExceptionHandler(MemberDuplicateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse memberDuplicateExceptionHandle(MemberDuplicateException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                getMessage("memberDuplicate.code"),
                getMessage("memberDuplicate.message")
        );
    }

    /**
     * Post, PostLike
     */
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

    @ExceptionHandler(PostLikeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse postLikeNotFoundExceptionHandle(PostLikeNotFoundException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.NOT_FOUND,
                getMessage("postLikeNotFound.code"),
                getMessage("postLikeNotFound.message")
        );
    }

    @ExceptionHandler(AlreadyPostLikeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse alreadyPostLikeExceptionHandle(AlreadyPostLikeException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                getMessage("alreadyPostLike.code"),
                getMessage("alreadyPostLike.message")
        );
    }

    @ExceptionHandler(NeverPostLikeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse neverPostLikeExceptionHandle(NeverPostLikeException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                getMessage("neverPostLikeException.code"),
                getMessage("neverPostLikeException.message")
        );
    }


    private String getMessage(String code) {
        return messageSource.getMessage(code, null, null);
    }
}
