package backend.main.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import backend.main.Filters.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    // private static final String[] PUBLIC_GET_ENDPOINTS = {
    // "/api/users/**",
    // "/api/order/**",
    // "/api/categories/**",
    // "/api/products/**"
    // };
    // private final String[] PUBLIC_POST_ENDPOINTS = {
    // "/api/auth/**",
    // };

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable()) // tắt CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Cho phép tất cả các endpoint mà bạn muốn public
                        .requestMatchers(
                                "/api/products/**",
                                "/api/categories/**",
                                "/api/image/**",
                                "/api/order/**",
                                "/api/users/**")
                        .permitAll()
                        // Các endpoint khác nếu có, cũng có thể permitAll hoặc authenticated
                        .anyRequest().permitAll());

        return http.build();
    }

}
