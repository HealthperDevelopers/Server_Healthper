package umc.healthper.exception.comment;

public class CommentUnauthorizedException extends RuntimeException {
    public CommentUnauthorizedException() {
        super();
    }

    public CommentUnauthorizedException(String message) {
        super(message);
    }

    public CommentUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentUnauthorizedException(Throwable cause) {
        super(cause);
    }

    protected CommentUnauthorizedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
