package konkuk.nServer.exception;

import konkuk.nServer.domain.user.exception.UserExceptionEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApiExceptionAdvice {

    /**
     * Request 받을 때, @Valid 에서 검증 실패할 때 발생
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class) // catch 할 exception
    public ApiExceptionResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        ApiExceptionResponse response = new ApiExceptionResponse("400", "검증 실패");
        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }

    /**
     * 로직 수행 중 오류
     */
    @ExceptionHandler({ApiException.class}) // catch 할 exception
    public ResponseEntity<ApiExceptionResponse> exceptionHandler(HttpServletRequest request, final ApiException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(e.getSecurityError().getStatus())
                .body(new ApiExceptionResponse(e.getSecurityError().getCode(), e.getSecurityError().getMessage()));
    }

    /*
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ApiExceptionEntity> exceptionHandler(HttpServletRequest request, final RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(ExceptionEnum.RUNTIME_EXCEPTION.getStatus())
                .body(ApiExceptionEntity.builder()
                        .errorCode(ExceptionEnum.RUNTIME_EXCEPTION.getCode())
                        .errorMessage(e.getMessage())
                        .build());
    }
     */

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiExceptionResponse> exceptionHandler(HttpServletRequest request, final Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(UserExceptionEnum.INTERNAL_SERVER_ERROR.getStatus())
                .body(new ApiExceptionResponse(UserExceptionEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMessage()));
    }
}