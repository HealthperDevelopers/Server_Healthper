package umc.healthper.exception.record;

public class EmptySectionException extends RuntimeException{
    public EmptySectionException() {
        super();
    }

    public EmptySectionException(String message) {
        super(message);
    }

    public EmptySectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptySectionException(Throwable cause) {
        super(cause);
    }

    protected EmptySectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
