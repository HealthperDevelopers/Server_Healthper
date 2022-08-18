package umc.healthper.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ExceptionResponse {

    private final LocalDateTime time;
    private final int status;
    private final String code;
    private final String message;

    public ExceptionResponse(HttpStatus status, String code, String message) {
        this.time = LocalDateTime.now();
        this.status = status.value();
        this.code = code;
        this.message = message;
    }
}

