package pl.twojekursy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pl.twojekursy.user.UserRole;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class SecurityConfig {

    //zad  dom - Comments
    // C - admin lub user
    // R - public
    // u - admin lub user
    // Lista - public

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests()

                .requestMatchers(HttpMethod.POST, "/api/users", "/api/posts/find")
                    .permitAll()
                .requestMatchers(HttpMethod.GET,
                        "/api/posts/*", "/api/posts",
                        "/api/comments", "/api/comments/*")
                    .permitAll()

                .requestMatchers(HttpMethod.POST, "/api/comments").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())
                .requestMatchers(HttpMethod.PUT, "/api/comments").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())

                .requestMatchers(HttpMethod.GET, "/api/groups-info").hasAuthority(UserRole.ADMIN.name())

                .anyRequest().authenticated()

                .and()
                .httpBasic();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
