package konkuk.nServer.error;

import konkuk.nServer.domain.user.error.UserExceptionEnum;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private UserExceptionEnum error;

    public ApiException(UserExceptionEnum e) {
        super(e.getMessage());
        this.error = e;
    }
}