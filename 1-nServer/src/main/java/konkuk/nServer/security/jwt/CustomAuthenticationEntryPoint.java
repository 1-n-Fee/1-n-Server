package konkuk.nServer.security.jwt;

import konkuk.nServer.exception.ExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        //String requestURI = request.getRequestURI();
        //log.info("requestUrl={}", requestURI);
        String exception = (String) request.getAttribute("exception");
        ExceptionEnum errorCode;

        log.debug("log: exception: {} ", exception);

        /*
         * 토큰 없는 경우
         */
        if (exception == null) {
            errorCode = ExceptionEnum.NOT_FOUND_TOKEN;
            setResponse(response, errorCode);
            return;
        }

        /*
         * 토큰 만료된 경우
         */
        if (exception.equals(ExceptionEnum.EXPIRED_TOKEN.getCode())) {
            errorCode = ExceptionEnum.EXPIRED_TOKEN;
            setResponse(response, errorCode);
            return;
        }

        /*
         * 토큰 시그니처가 다른 경우
         */
        if (exception.equals(ExceptionEnum.INVALID_TOKEN.getCode())) {
            errorCode = ExceptionEnum.INVALID_TOKEN;
            setResponse(response, errorCode);
        }
    }

    /*
     * 한글 출력을 위해 getWriter() 사용
     */
    private void setResponse(HttpServletResponse response, ExceptionEnum errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("{ \"errorCode\" : \"" + errorCode.getCode()
                + "\", \"message\" : \"" + errorCode.getMessage() + "\" }");
    }

}
