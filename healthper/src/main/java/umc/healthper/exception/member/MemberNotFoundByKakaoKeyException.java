package umc.healthper.exception.member;

public class MemberNotFoundByKakaoKeyException extends RuntimeException {
    public MemberNotFoundByKakaoKeyException() {
        super();
    }

    public MemberNotFoundByKakaoKeyException(String message) {
        super(message);
    }

    public MemberNotFoundByKakaoKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberNotFoundByKakaoKeyException(Throwable cause) {
        super(cause);
    }

    protected MemberNotFoundByKakaoKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
