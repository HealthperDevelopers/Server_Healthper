package umc.healthper.exception.member;

public class MemberDuplicateException extends RuntimeException {
    public MemberDuplicateException() {
        super();
    }

    public MemberDuplicateException(String message) {
        super(message);
    }

    public MemberDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberDuplicateException(Throwable cause) {
        super(cause);
    }

    protected MemberDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
