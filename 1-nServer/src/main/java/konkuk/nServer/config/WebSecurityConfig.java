package konkuk.nServer.config;

import com.example.demo.repository.EmailPasswordRepository;
import com.example.demo.security.*;
import konkuk.nServer.domain.user.repository.UserRepository;
import konkuk.nServer.security.jwt.JwtAuthenticationFilter;
import konkuk.nServer.security.jwt.JwtAuthorizationFilter;
import konkuk.nServer.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {

    private final UserRepository userRepository;
    private final EmailPasswordRepository emailPasswordRepository;
    private final JwtTokenProvider tokenProvider;
    private final UserOAuth2Service userOAuth2Service;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http 시큐리티 빌더
        return http.cors() // cors 허용
                .and()
                .csrf().disable() // csrf는 현재 사용하지 않으므로 disable
                .httpBasic().disable() // token을 사용하므로 basic 인증 disable
                .formLogin().disable()
                .apply(new MyCustomDsl()) // 커스텀 필터 등록
                .and()
                .headers().frameOptions().disable()// h2 오류 방지

                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //session 기반이 아님을 선언

                .and()
                .authorizeRequests(authorize ->
                        authorize.antMatchers("/admin")
                                .access("hasRole('ADMIN')")
                                .antMatchers("/user")
                                .access("hasRole('USER') or hasRole('ADMIN')")
                                //.antMatchers("/home").authenticated()
                                .anyRequest().permitAll()
                )
                //.antMatchers("/home", "/signup", "/login", "/h2-console/**").permitAll()
                //.anyRequest().authenticated() //이외의 모든 경로는 인증 해야 함
                //.and().build();
                .build();

    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http.addFilter(new JwtAuthenticationFilter(authenticationManager, tokenProvider))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository, emailPasswordRepository, tokenProvider));
        }
    }

    // CORS 허용 설정 Bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.addAllowedOriginPattern("*");
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

