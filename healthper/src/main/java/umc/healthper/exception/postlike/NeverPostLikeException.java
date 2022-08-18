package umc.healthper.exception.postlike;

public class NeverPostLikeException extends RuntimeException {
    public NeverPostLikeException() {
        super();
    }

    public NeverPostLikeException(String message) {
        super(message);
    }

    public NeverPostLikeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NeverPostLikeException(Throwable cause) {
        super(cause);
    }

    protected NeverPostLikeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
