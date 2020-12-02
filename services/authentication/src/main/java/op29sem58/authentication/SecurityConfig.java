package op29sem58.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MyUserDetailsService userDetailsService;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/login")
                .permitAll()

                .antMatchers(
                        "/getMyCourses", "/getSchedule", "/allMyLectures",
                        "/allMyCourses", "/user", "/declineOnCampusOffer/**", "/setPreferences/**")
                .hasAnyRole("STUDENT", "TEACHER", "ADMIN")

                .antMatchers(
                        "/moveLectureOnline", "/scheduleLecturesUntil", "/scheduleLecture",
                        "/cancelLecture/**", "/assignStudentsUntil"
                ).hasAnyRole("TEACHER", "ADMIN")

                .antMatchers(
                        "/users", "/users/**", "/modifyUser", "/createCourse", "/modifyCourse",
                        "/createLecture", "/createRoom", "/modifyRoom", "/deleteRoom/**")
                .hasAnyRole("ADMIN")

                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}