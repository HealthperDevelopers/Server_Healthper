package umc.healthper.exception.commentlike;

public class CommentLikeNotFoundException extends RuntimeException {
    public CommentLikeNotFoundException() {
        super();
    }

    public CommentLikeNotFoundException(String message) {
        super(message);
    }

    public CommentLikeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentLikeNotFoundException(Throwable cause) {
        super(cause);
    }

    protected CommentLikeNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
