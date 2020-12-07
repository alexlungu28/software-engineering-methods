package op29sem58.authentication;

import io.jsonwebtoken.Jwts;
import op29sem58.authentication.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private transient AuthenticationManager authenticationManager;

    @Autowired
    private transient MyUserDetailsService userDetailsService;

    @Autowired
    private transient JwtUtil jwtTokenUtil;

    /** Creates a token for the user if the provided credentials are correct.
     *
     * @return the token
     * @throws Exception if the provided username or password is incorrect
     */
    @PostMapping("/login")
    public String createAuthenticationToken(@RequestBody User user) {
        String id = user.getNetid();
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

    @GetMapping("/isStudent")
    public boolean isStudent() {
        return true;
    }

    @GetMapping("/isTeacher")
    public boolean isTeacher() {
        return true;
    }

    @GetMapping("/isAdmin")
    public boolean isAdmin() {
        return true;
    }

    @GetMapping("/getUsername")
    public String getUsername(@RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        return Jwts.parser().setSigningKey("secret").parseClaimsJws(token).getBody().getSubject();
    }
}
