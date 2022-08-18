package umc.healthper.exception.postlike;

public class PostLikeNotFoundException extends RuntimeException {
    public PostLikeNotFoundException() {
        super();
    }

    public PostLikeNotFoundException(String message) {
        super(message);
    }

    public PostLikeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostLikeNotFoundException(Throwable cause) {
        super(cause);
    }

    protected PostLikeNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
