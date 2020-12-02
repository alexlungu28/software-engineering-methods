package op29sem58.authentication;

import op29sem58.authentication.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    /** Creates a token for the user if the provided credentials are correct.
     *
     * @return the token
     * @throws Exception if the provided username or password is incorrect
     */
    @PostMapping("/login")
    public String createAuthenticationToken(@RequestBody User user) {
        String id = user.getNetID();
        String password = user.getPassword();
        password = "" + password.hashCode();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    id, password));
        } catch (BadCredentialsException e) {
            return "Incorrect username or password";
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(id);

        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return jwt;
    }
}
