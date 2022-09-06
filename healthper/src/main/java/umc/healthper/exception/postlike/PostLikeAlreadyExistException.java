package umc.healthper.exception.postlike;

public class PostLikeAlreadyExistException extends RuntimeException {
    public PostLikeAlreadyExistException() {
        super();
    }

    public PostLikeAlreadyExistException(String message) {
        super(message);
    }

    public PostLikeAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostLikeAlreadyExistException(Throwable cause) {
        super(cause);
    }

    protected PostLikeAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
