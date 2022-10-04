package umc.healthper.exception.completeExercise;

public class NotMatchOwnerException extends RuntimeException{
    public NotMatchOwnerException() {
    }

    public NotMatchOwnerException(String message) {
        super(message);
    }

    public NotMatchOwnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotMatchOwnerException(Throwable cause) {
        super(cause);
    }

    public NotMatchOwnerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
