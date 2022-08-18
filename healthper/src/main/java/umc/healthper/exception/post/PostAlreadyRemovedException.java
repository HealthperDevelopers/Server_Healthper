package umc.healthper.exception.post;

public class PostAlreadyRemovedException extends RuntimeException {
    public PostAlreadyRemovedException() {
        super();
    }

    public PostAlreadyRemovedException(String message) {
        super(message);
    }

    public PostAlreadyRemovedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostAlreadyRemovedException(Throwable cause) {
        super(cause);
    }

    protected PostAlreadyRemovedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
