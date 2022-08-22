package umc.healthper.exception.record;

public class RecordNotFoundByIdException extends RuntimeException{
    public RecordNotFoundByIdException() {
        super();
    }

    public RecordNotFoundByIdException(String message) {
        super(message);
    }

    public RecordNotFoundByIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecordNotFoundByIdException(Throwable cause) {
        super(cause);
    }

    protected RecordNotFoundByIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
