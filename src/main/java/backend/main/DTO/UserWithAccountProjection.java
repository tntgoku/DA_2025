package backend.main.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface UserWithAccountProjection {
    Integer getIdUser();
    Integer getAccountId();
    String getFullName();
    String getPhone();
    String getEmail();
    Boolean getEmailVerified();
    Boolean getPhoneVerified();
    LocalDate getDateOfBirth();
    String getGender();
    String getAddress();
    Integer getTotalOrders();
    BigDecimal getTotalSpent();
    String getNotes();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    
    // Account information
    String getUsername();
    String getRoleName();
    Long getRoleId();
    Boolean getStatus();
}
