package umc.healthper.exception.member;

public class MemberNotFoundByIdException extends RuntimeException {
    public MemberNotFoundByIdException() {
        super();
    }

    public MemberNotFoundByIdException(String message) {
        super(message);
    }

    public MemberNotFoundByIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberNotFoundByIdException(Throwable cause) {
        super(cause);
    }

    protected MemberNotFoundByIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
