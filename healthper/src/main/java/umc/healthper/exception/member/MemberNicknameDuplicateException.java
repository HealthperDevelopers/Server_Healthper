package umc.healthper.exception.member;

public class MemberNicknameDuplicateException extends RuntimeException {
    public MemberNicknameDuplicateException() {
        super();
    }

    public MemberNicknameDuplicateException(String message) {
        super(message);
    }
}
