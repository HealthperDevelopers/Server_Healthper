package umc.healthper.exception.member;

public class AlreadyResignedMemberException extends RuntimeException {
    public AlreadyResignedMemberException() {
        super();
    }

    public AlreadyResignedMemberException(String message) {
        super(message);
    }
}
