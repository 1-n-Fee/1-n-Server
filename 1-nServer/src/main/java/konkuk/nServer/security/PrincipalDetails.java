package konkuk.nServer.security;

import konkuk.nServer.domain.account.domain.Password;
import konkuk.nServer.domain.user.domain.Role;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class PrincipalDetails implements UserDetails {

    private String password;
    private Role role;
    private Long id;
    Collection<GrantedAuthority> authorities = new ArrayList<>();

    public PrincipalDetails(Long id, Password password, Role role) {
        this.id = id;
        this.password = password.getPassword();
        this.role = role;
        authorities.add(role::name);
    }

    public PrincipalDetails(Long id, Role role) {
        this.id = id;
        this.role = role;
        authorities.add(role::name);
    }

    //계정이 갖고있는 권한 목록은 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.id.toString();
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
