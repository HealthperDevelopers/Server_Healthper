package umc.healthper.exception.comment;

public class CommentAlreadyRemovedException extends RuntimeException {
    public CommentAlreadyRemovedException() {
        super();
    }

    public CommentAlreadyRemovedException(String message) {
        super(message);
    }

    public CommentAlreadyRemovedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentAlreadyRemovedException(Throwable cause) {
        super(cause);
    }

    protected CommentAlreadyRemovedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
