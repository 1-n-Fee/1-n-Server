package konkuk.nServer.security;

import konkuk.nServer.domain.user.domain.Password;
import konkuk.nServer.domain.user.domain.Role;
import konkuk.nServer.domain.user.domain.Storemanager;
import konkuk.nServer.domain.user.domain.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class PrincipalDetails implements UserDetails {
    private User user = null;
    private Storemanager storemanager = null;
    private Password password = null;
    private Role role;

    public PrincipalDetails(User user, Password password) {
        this.user = user;
        this.password = password;
        this.role = Role.ROLE_STUDENT;
    }

    public PrincipalDetails(Storemanager storemanager, Password password) {
        this.storemanager = storemanager;
        this.role = Role.ROLE_STOREMANAGER;
        this.password = password;
    }

    //계정이 갖고있는 권한 목록은 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (user != null) {
            String role = user.getRole().toString();
            authorities.add(() -> role);
        } else if (storemanager != null) {
            String role = storemanager.getRole().toString();
            authorities.add(() -> role);
        }
        return authorities;
    }

    public Long getId() {
        if (role == Role.ROLE_STUDENT) return user.getId();
        else return storemanager.getId();
    }

    @Override
    public String getPassword() {
        return password.getPassword();
    }

    @Override
    public String getUsername() {
        if (role == Role.ROLE_STUDENT) return user.getEmail();
        else return storemanager.getEmail();
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
