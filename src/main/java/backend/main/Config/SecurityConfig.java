package backend.main.Config;

import backend.main.Filters.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - không cần xác thực
                        .requestMatchers("/api/authz/login", "/api/authz/register", "/api/authz/forgot-password", "/api/authz/reset-password").permitAll()
                        
                        // Endpoint để tạo admin user (tạm thời cho phép public)
                        .requestMatchers("/api/user/make-admin").permitAll()
                        
                        // Logout endpoint - cần xác thực để logout
                        .requestMatchers("/api/authz/logout").authenticated()
                        
                        // Public product/category endpoints - cho phép xem sản phẩm
                        .requestMatchers("/api/products/**", "/api/categories/**", "/api/image/**").permitAll()
                        
                        // Discount calculation endpoints - cho phép tính toán giảm giá
                        .requestMatchers("/api/discounts/**").permitAll()
                                        // Protected endpoints - cần xác thực JWT
                        .requestMatchers("/api/authz/profile").authenticated()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/order/**").permitAll()
                        .requestMatchers("/api/voucher/**").permitAll()
                        // authenticated()
// authenticated()
                        // Admin endpoints - cần role ADMIN
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        
                        // Các endpoint khác → cho phép
                        .anyRequest().permitAll())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
