package backend.main.Service;

import backend.main.Config.LoggerE;
import backend.main.Model.Account;
import backend.main.Model.OtpCode;
import backend.main.Repository.AccountRepository;
import backend.main.Repository.OtpCodeRepository;
import backend.main.Request.ResetPasswordWithOtpRequest;
import backend.main.Service.EmailService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.main.Model.ResponseObject;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {
    
    @Autowired
    private OtpCodeRepository otpCodeRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder pEncoder;
    
    private static final org.slf4j.Logger logger = LoggerE.getLogger();
    
    // Thời gian hết hạn OTP (1 phút)
    private static final int OTP_EXPIRY_MINUTES = 1;
    
    // Thời gian giới hạn gửi lại OTP (1 phút)
    private static final int RESEND_LIMIT_MINUTES = 1;
    

    public boolean findAccount(String username){
        Optional<Account> findOptional=accountRepository.findByUsername(username);
        return findOptional.isPresent();
    }
    /**
     * Tạo và gửi OTP cho quên mật khẩu
     */
    @Transactional
    public ResponseEntity<ResponseObject> generateAndSendForgotPasswordOtp(String email) {
        try {
            logger.info("Generating OTP for forgot password: {}", email);
            
            // Kiểm tra xem có OTP nào đang còn hiệu lực không
            Optional<OtpCode> existingOtp = otpCodeRepository.findValidOtpByEmailAndType(email, "FORGOT_PASSWORD");
            if (existingOtp.isPresent()) {
                OtpCode otp = existingOtp.get();
                if (!otp.canResend()) {
                    long remainingSeconds = 60 - java.time.Duration.between(otp.getLastSentAt(), LocalDateTime.now()).getSeconds();
                    return new ResponseEntity<>(
                        new ResponseObject(429, "Vui lòng chờ " + remainingSeconds + " giây trước khi gửi lại OTP", 1, null),
                        HttpStatus.TOO_MANY_REQUESTS
                    );
                }
            }
            
            // Tạo OTP mới
            String otpCode = generateOtpCode();
            LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);
            
            // Lưu OTP vào database
            OtpCode otp = new OtpCode(email, otpCode, "FORGOT_PASSWORD", expiresAt);
            otpCodeRepository.save(otp);
            
            // Gửi email OTP
            emailService.sendOtpEmail(email, otpCode, "Quên mật khẩu");
            
            logger.info("OTP sent successfully to: {}", email);
            
            return new ResponseEntity<>(
                new ResponseObject(200, "OTP đã được gửi đến email của bạn", 0, "Vui lòng kiểm tra email"),
                HttpStatus.OK
            );
            
        } catch (Exception e) {
            logger.error("Error generating OTP for {}: {}", email, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(500, "Có lỗi xảy ra khi gửi OTP", 1, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    
    /**
     * Xác thực OTP cho quên mật khẩu
     */
    @Transactional
    public ResponseEntity<ResponseObject> verifyForgotPasswordOtp(String email, String otpCode) {
        try {
            logger.info("Verifying OTP for forgot password: {} - {}", email, otpCode);
            
            Optional<OtpCode> otpOptional = otpCodeRepository.findByCodeAndEmailAndType(otpCode, email, "FORGOT_PASSWORD");
            
            if (!otpOptional.isPresent()) {
                return new ResponseEntity<>(
                    new ResponseObject(400, "Mã OTP không hợp lệ", 1, null),
                    HttpStatus.BAD_REQUEST
                );
            }
            
            OtpCode otp = otpOptional.get();
            
            // Kiểm tra OTP đã được sử dụng chưa
            if (otp.getIsUsed()) {
                return new ResponseEntity<>(
                    new ResponseObject(400, "Mã OTP đã được sử dụng", 1, null),
                    HttpStatus.BAD_REQUEST
                );
            }
            
            // Kiểm tra OTP có hết hạn không
            if (otp.isExpired()) {
                return new ResponseEntity<>(
                    new ResponseObject(400, "Mã OTP đã hết hạn", 1, null),
                    HttpStatus.BAD_REQUEST
                );
            }
            
            // // Kiểm tra số lần thử
            // if (otp.getAttemptCount() >= otp.getMaxAttempts()) {
            //     return new ResponseEntity<>(
            //         new ResponseObject(400, "Đã vượt quá số lần thử cho phép", 1, null),
            //         HttpStatus.BAD_REQUEST
            //     );
            // }
            
            // // Tăng số lần thử
            // otp.incrementAttemptCount();
            // otpCodeRepository.save(otp);
            
            // Kiểm tra mã OTP
            if (!otp.getCode().equals(otpCode)) {
                return new ResponseEntity<>(
                    new ResponseObject(400, "Mã OTP không chính xác", 1, null),
                    HttpStatus.BAD_REQUEST
                );
            }
            
            // Đánh dấu OTP đã được sử dụng
            otp.markAsUsed();
            otpCodeRepository.save(otp);
            
            logger.info("OTP verified successfully for: {}", email);
            
            return new ResponseEntity<>(
                new ResponseObject(200, "Xác thực OTP thành công", 0, "Bạn có thể đặt mật khẩu mới"),
                HttpStatus.OK
            );
            
        } catch (Exception e) {
            logger.error("Error verifying OTP for {}: {}", email, e.getMessage(), e);
            return new ResponseEntity<>(
                new ResponseObject(500, "Có lỗi xảy ra khi xác thực OTP", 1, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    
    /**
     * Tạo mã OTP ngẫu nhiên 6 chữ số
     */
    private String generateOtpCode() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Tạo số từ 100000 đến 999999
        return String.valueOf(otp);
    }
    
    /**
     * Kiểm tra xem có thể gửi OTP mới không
     */
    public boolean canSendNewOtp(String email, String otpType) {
        Optional<OtpCode> latestOtp = otpCodeRepository.findLatestOtpByEmailAndType(email, otpType);
        
        if (!latestOtp.isPresent()) {
            return true;
        }
        
        OtpCode otp = latestOtp.get();
        return otp.canResend();
    }
    
    /**
     * Lấy thời gian còn lại trước khi có thể gửi OTP mới
     */
    public long getRemainingTimeForResend(String email, String otpType) {
        Optional<OtpCode> latestOtp = otpCodeRepository.findLatestOtpByEmailAndType(email, otpType);
        
        if (!latestOtp.isPresent()) {
            return 0;
        }
        
        OtpCode otp = latestOtp.get();
        if (otp.getLastSentAt() == null) {
            return 0;
        }
        
        LocalDateTime nextSendTime = otp.getLastSentAt().plusMinutes(RESEND_LIMIT_MINUTES);
        LocalDateTime now = LocalDateTime.now();
        
        if (now.isAfter(nextSendTime)) {
            return 0;
        }
        
        return java.time.Duration.between(now, nextSendTime).getSeconds();
    }
    

    public ResponseEntity<ResponseObject> savePassword(ResetPasswordWithOtpRequest entity){
        
        Optional<Account> accOptional=accountRepository.findByUsername(entity.getEmail());
        String encode=pEncoder.encode(entity.getNewPassword());
        accOptional.get().setPasswordHash(encode);
        return new ResponseEntity<>(new ResponseObject(200, "Đổi mật khẩu thành công", OTP_EXPIRY_MINUTES, encode),HttpStatus.OK);
    }
    /**
     * Dọn dẹp các OTP đã hết hạn
     */
    @Transactional
    public void cleanupExpiredOtps() {
        try {
            otpCodeRepository.deleteExpiredOtps();
            logger.info("Cleaned up expired OTPs");
        } catch (Exception e) {
            logger.error("Error cleaning up expired OTPs: {}", e.getMessage());
        }
    }
}
