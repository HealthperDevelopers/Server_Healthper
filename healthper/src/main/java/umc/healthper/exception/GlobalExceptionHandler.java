package umc.healthper.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import umc.healthper.exception.block.MemberBlockDuplicateException;
import umc.healthper.exception.block.MemberBlockNotFoundException;
import umc.healthper.exception.comment.CommentAlreadyRemovedException;
import umc.healthper.exception.comment.CommentNotFoundException;
import umc.healthper.exception.comment.CommentUnauthorizedException;
import umc.healthper.exception.commentlike.CommentLikeAlreadyExistException;
import umc.healthper.exception.commentlike.CommentLikeNotFoundException;
import umc.healthper.exception.member.MemberDuplicateException;
import umc.healthper.exception.member.MemberNotFoundByIdException;
import umc.healthper.exception.member.MemberNotFoundByKakaoKeyException;
import umc.healthper.exception.post.PostAlreadyRemovedException;
import umc.healthper.exception.post.PostNotFoundException;
import umc.healthper.exception.post.PostUnauthorizedException;
import umc.healthper.exception.postlike.PostLikeAlreadyExistException;
import umc.healthper.exception.postlike.PostLikeNotFoundException;
import umc.healthper.exception.record.EmptySectionException;
import umc.healthper.exception.record.RecordNotFoundByIdException;

import java.util.Locale;

import javax.validation.ConstraintViolationException;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Global
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class,
            MissingServletRequestParameterException.class, MismatchedInputException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse badRequestExceptionHandle(Exception e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST,
                getMessage("badRequest.code"),
                getMessage("badRequest.message")
        );
    }

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
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse memberDuplicateExceptionHandle(MemberDuplicateException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.CONFLICT,
                getMessage("memberDuplicate.code"),
                getMessage("memberDuplicate.message")
        );
    }

    @ExceptionHandler(MemberBlockNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse memberBlockNotFoundExceptionHandle(MemberBlockNotFoundException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.NOT_FOUND,
                getMessage("memberBlockNotFound.code"),
                getMessage("memberBlockNotFound.message")
        );
    }

    @ExceptionHandler(MemberBlockDuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse memberBlockDuplicateExceptionHandle(MemberBlockDuplicateException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.CONFLICT,
                getMessage("memberBlockDuplicate.code"),
                getMessage("memberBlockDuplicate.message")
        );
    }

    /**
     * record
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse nonRecord(RecordNotFoundByIdException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST,
                getMessage("recordNotFound.code"),
                getMessage("recordNotFound.message")
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse emptySection(EmptySectionException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST,
                getMessage("sectionEmpty.code"),
                getMessage("sectionEmpty.message")
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
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse postAlreadyRemovedExceptionHandle(PostAlreadyRemovedException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.CONFLICT,
                getMessage("postAlreadyRemoved.code"),
                getMessage("postAlreadyRemoved.message")
        );
    }

    @ExceptionHandler(PostUnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse postUnauthorizedExceptionHandle(PostUnauthorizedException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.UNAUTHORIZED,
                getMessage("postUnauthorized.code"),
                getMessage("postUnauthorized.message")
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

    @ExceptionHandler(PostLikeAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse alreadyPostLikeExceptionHandle(PostLikeAlreadyExistException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.CONFLICT,
                getMessage("postLikeAlreadyExist.code"),
                getMessage("postLikeAlreadyExist.message")
        );
    }

    /**
     * Comment, CommentLike
     */
    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse commentNotFoundExceptionHandle(CommentNotFoundException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.NOT_FOUND,
                getMessage("commentNotFound.code"),
                getMessage("commentNotFound.message")
        );
    }

    @ExceptionHandler(CommentAlreadyRemovedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse commentAlreadyRemovedExceptionHandle(CommentAlreadyRemovedException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.CONFLICT,
                getMessage("commentAlreadyRemoved.code"),
                getMessage("commentAlreadyRemoved.message")
        );
    }

    @ExceptionHandler(CommentUnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse postUnauthorizedExceptionHandle(CommentUnauthorizedException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.UNAUTHORIZED,
                getMessage("commentUnauthorized.code"),
                getMessage("commentUnauthorized.message")
        );
    }

    @ExceptionHandler(CommentLikeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse commentLikeNotFoundExceptionHandle(CommentLikeNotFoundException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.NOT_FOUND,
                getMessage("commentLikeNotFound.code"),
                getMessage("commentLikeNotFound.message")
        );
    }

    @ExceptionHandler(CommentLikeAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse commentLikeAlreadyExistExceptionHandle(CommentLikeAlreadyExistException e) {
        log.error(String.valueOf(e));
        return new ExceptionResponse(
                HttpStatus.CONFLICT,
                getMessage("commentLikeAlreadyExist.code"),
                getMessage("commentLikeAlreadyExist.message")
        );
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.getDefault());
    }
}
