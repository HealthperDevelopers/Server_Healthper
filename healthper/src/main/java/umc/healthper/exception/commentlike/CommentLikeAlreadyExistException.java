package umc.healthper.exception.commentlike;

public class CommentLikeAlreadyExistException extends RuntimeException {
    public CommentLikeAlreadyExistException() {
        super();
    }

    public CommentLikeAlreadyExistException(String message) {
        super(message);
    }

    public CommentLikeAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentLikeAlreadyExistException(Throwable cause) {
        super(cause);
    }

    protected CommentLikeAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
