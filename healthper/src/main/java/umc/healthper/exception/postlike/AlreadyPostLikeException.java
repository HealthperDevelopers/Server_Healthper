package umc.healthper.exception.postlike;

public class AlreadyPostLikeException extends RuntimeException {
    public AlreadyPostLikeException() {
        super();
    }

    public AlreadyPostLikeException(String message) {
        super(message);
    }

    public AlreadyPostLikeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyPostLikeException(Throwable cause) {
        super(cause);
    }

    protected AlreadyPostLikeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
