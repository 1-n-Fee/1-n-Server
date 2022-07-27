package konkuk.nServer.security;

import com.example.demo.domain.EmailPassword;
import com.example.demo.domain.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class PrincipalDetails implements UserDetails {
    private User user;
    private EmailPassword emailPassword;

    public PrincipalDetails(EmailPassword emailPassword, User user) {
        this.user = user;
        this.emailPassword = emailPassword;
    }

    //계정이 갖고있는 권한 목록은 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        String role = user.getRole().toString();
        authorities.add(() -> role);
        return authorities;
    }

    @Override
    public String getPassword() {
        return emailPassword.getPassword();
    }

    @Override
    public String getUsername() {
        return emailPassword.getEmail();
    }

    //계정이 만료되지 않았는지 리턴 (true: 만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겨있는지 않았는지 리턴. (true:잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호가 만료되지 않았는지 리턴한다. (true:만료안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화(사용가능)인지 리턴 (true:활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
