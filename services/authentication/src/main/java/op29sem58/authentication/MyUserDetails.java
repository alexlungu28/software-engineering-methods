package op29sem58.authentication;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import op29sem58.authentication.roles.Role;
import op29sem58.authentication.users.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails implements UserDetails {

    public static final long serialVersionUID = 4328743;

    private transient String netid;
    private String username;
    private transient String password;
    private boolean active;
    private transient List<GrantedAuthority> authorities;
    private transient Role role;

    public MyUserDetails() {

    }

    /** Constructor for MyUserDetails.
     *
     * @param user - the user to get the details from - also sets authority to their role
     */
    public MyUserDetails(User user) {
        this.netid = user.getNetid();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.authorities = Arrays.stream(user.getRole().getName().split(" "))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return netid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
