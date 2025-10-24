package backend.main.Repository;

import backend.main.Model.OtpCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface OtpCodeRepository extends BaseRepository<OtpCode, Integer> {
    
    // Tìm OTP code chưa sử dụng và chưa hết hạn theo email và loại
    @Query("SELECT o FROM OtpCode o WHERE o.email = :email AND o.otpType = :otpType AND o.isUsed = false AND o.expiresAt > CURRENT_TIMESTAMP ORDER BY o.createdAt DESC")
    Optional<OtpCode> findValidOtpByEmailAndType(@Param("email") String email, @Param("otpType") String otpType);
    
    // Tìm OTP code theo email và loại (bao gồm cả đã sử dụng)
    @Query("SELECT o FROM OtpCode o WHERE o.email = :email AND o.otpType = :otpType ORDER BY o.createdAt DESC")
    List<OtpCode> findByEmailAndType(@Param("email") String email, @Param("otpType") String otpType);
    
    // Tìm OTP code gần nhất theo email và loại
    @Query("SELECT o FROM OtpCode o WHERE o.email = :email AND o.otpType = :otpType ORDER BY o.createdAt DESC")
    Optional<OtpCode> findLatestOtpByEmailAndType(@Param("email") String email, @Param("otpType") String otpType);
    
    // Đếm số lượng OTP đã gửi trong khoảng thời gian
    @Query("SELECT COUNT(o) FROM OtpCode o WHERE o.email = :email AND o.otpType = :otpType AND o.createdAt >= :startTime")
    Long countOtpSentInTimeRange(@Param("email") String email, @Param("otpType") String otpType, @Param("startTime") java.time.LocalDateTime startTime);
    
    // Xóa các OTP đã hết hạn
    @Query("DELETE FROM OtpCode o WHERE o.expiresAt < CURRENT_TIMESTAMP")
    void deleteExpiredOtps();
    
    // Tìm OTP theo code và email
    @Query("SELECT o FROM OtpCode o WHERE o.code = :code AND o.email = :email AND o.otpType = :otpType")
    Optional<OtpCode> findByCodeAndEmailAndType(@Param("code") String code, @Param("email") String email, @Param("otpType") String otpType);
}
