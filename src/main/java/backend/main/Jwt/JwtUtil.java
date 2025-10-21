package backend.main.Jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backend.main.Config.JwtConfig;
import backend.main.Model.Account;
import backend.main.Model.User.*;
import backend.main.Repository.AccountRepository;

import java.util.*;
import java.util.function.Function;

import io.jsonwebtoken.Claims;

@Component
public class JwtUtil {

    @Autowired
    private AccountRepository accountRepository;
    private final JwtConfig jwtConfig;

    public JwtUtil(JwtConfig jwtConfig2) {
        this.jwtConfig = jwtConfig2;
    }

    // Tạo token từ User
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        if (user.getAccount() != null) {
            // Lấy account từ DB
            Account account = accountRepository.findById(user.getAccount())
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            if (account.getRole() != null) {

                claims.put("role", "test");
            }
        }
        return createToken(claims, user.getEmail());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret())
                .compact();
    }

    // Lấy username (email) từ token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Kiểm tra token có hợp lệ hay không
    public boolean validateToken(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getEmail()) && !isTokenExpired(token));
    }

    public boolean validateToken(String token, String email) {
        final String username = extractUsername(token);
        return (username.equals(email) && !isTokenExpired(token));
    }

    // Kiểm tra token hết hạn chưa
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtConfig.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }
}
