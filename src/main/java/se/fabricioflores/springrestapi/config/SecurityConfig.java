package se.fabricioflores.springrestapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import se.fabricioflores.springrestapi.service.IUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final IUserService userService;
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(@Lazy IUserService userService, JwtFilter jwtFilter) {
        this.userService = userService;
        this.jwtFilter = jwtFilter;
    }

    // ** Tells the auth provider to use our custom user service and password encoder for authentication
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // ** Password encoder using BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ** Base path e.g /api/ should NOT be added to the request matchers
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,"/auth/login", "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET,"/auth/welcome").hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(HttpMethod.GET,"/categories", "/categories/*").permitAll()
                        .requestMatchers(HttpMethod.POST,"/categories", "/categories/").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/locations/public", "/locations/public/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/locations/user").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/locations/nearby").permitAll()
                        .requestMatchers(HttpMethod.GET, "/locations", "/locations/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/locations").hasAnyAuthority("ADMIN", "USER")
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
