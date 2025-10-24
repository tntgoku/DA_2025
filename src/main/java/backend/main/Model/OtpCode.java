package backend.main.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_codes")
@Getter
@Setter
public class OtpCode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 100)
    private String email;
    
    @Column(nullable = false, length = 6)
    private String code;
    
    @Column(name = "otp_type", nullable = false, length = 20)
    private String otpType; // FORGOT_PASSWORD, EMAIL_VERIFICATION, etc.
    
    @Column(name = "is_used", nullable = false)
    private Boolean isUsed = false;
    
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "last_sent_at")
    private LocalDateTime lastSentAt;
    
    @Column(name = "attempt_count", nullable = false)
    private Integer attemptCount = 0;
    
    @Column(name = "max_attempts", nullable = false)
    private Integer maxAttempts = 3;
    
    // Constructor
    public OtpCode() {
    }
    
    public OtpCode(String email, String code, String otpType, LocalDateTime expiresAt) {
        this.email = email;
        this.code = code;
        this.otpType = otpType;
        this.expiresAt = expiresAt;
        this.createdAt = LocalDateTime.now();
        this.lastSentAt = LocalDateTime.now();
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
    
    public boolean canResend() {
        if (this.lastSentAt == null) {
            return true;
        }
        return LocalDateTime.now().isAfter(this.lastSentAt.plusMinutes(1));
    }
    public boolean isValid() {
        return !this.isUsed && !this.isExpired() && this.attemptCount < this.maxAttempts;
    }
    
    public void markAsUsed() {
        this.isUsed = true;
    }
    
    public void incrementAttemptCount() {
        this.attemptCount++;
    }
    
    public void updateLastSentTime() {
        this.lastSentAt = LocalDateTime.now();
    }
}
