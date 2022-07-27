package konkuk.nServer.error;

import konkuk.nServer.domain.user.error.UserExceptionEnum;
import konkuk.nServer.security.error.SecurityExceptionEnum;
import lombok.Getter;

@Getter
public class ApiException extends IllegalArgumentException {
    private UserExceptionEnum userError;
    private SecurityExceptionEnum securityError;

    public ApiException(UserExceptionEnum e) {
        super(e.getMessage());
        this.userError = e;
    }

    public ApiException(SecurityExceptionEnum e) {
        super(e.getMessage());
        this.securityError = e;
    }
}