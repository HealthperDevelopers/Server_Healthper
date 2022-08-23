package umc.healthper.exception.post;

public class PostUnauthorizedException extends RuntimeException {
    public PostUnauthorizedException() {
        super();
    }

    public PostUnauthorizedException(String message) {
        super(message);
    }

    public PostUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostUnauthorizedException(Throwable cause) {
        super(cause);
    }

    protected PostUnauthorizedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
