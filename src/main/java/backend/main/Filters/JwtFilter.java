package backend.main.Filters;

import backend.main.Config.LoggerE;
import backend.main.DTO.AuthzProjection;
import backend.main.Repository.UserRepository;
import backend.main.Jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/authz/login")
                || path.startsWith("/api/authz/register")
                || path.startsWith("/api/users")
                || path.startsWith("/api/products")
                || path.startsWith("/api/categories")
                || path.startsWith("/api/order");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            logger.info("Token dc lay la:{}" , token);
            try {
                // Kiểm tra xem token có được encode base64 không
                String decodedToken = token;
                try {
                    // Thử decode base64
                    byte[] decodedBytes = java.util.Base64.getDecoder().decode(token);
                    decodedToken = new String(decodedBytes, "UTF-8");
                    logger.info("Token đã được decode từ base64");
                } catch (Exception e) {
                    // Nếu không phải base64, sử dụng token gốc
                    logger.info("Token không phải base64, sử dụng token gốc");
                }
                
                String email = jwtUtil.extractUsername(decodedToken);
                logger.info("🟢 Email trong token: {}" , email);
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    Optional<AuthzProjection> userOpt = userRepository.findByEmailWithAccountAndRole(email);

                    if (userOpt.isPresent() && jwtUtil.validateToken(decodedToken, email)) {

                        AuthzProjection user = userOpt.get();
                        String roleName = (user.getRoleName() != null) ? user.getRoleName() : "USER";
                        logger.info("Role: {}" , roleName);
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                user.getEmail(),
                                null,
                                List.of(new SimpleGrantedAuthority(roleName)));

                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        request.setAttribute("email", email);
                    }
                }

            } catch (Exception e) {
                logger.info("Token không hợp lệ: {}" , e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

}
