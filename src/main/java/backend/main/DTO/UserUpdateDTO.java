package backend.main.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UserUpdateDTO {
    private Integer id;
    // private Integer account; // Không cho phép thay đổi để tránh mất liên kết
    private String fullName;
    private String phone;
    private String email;
    private Boolean emailVerified;
    private Boolean phoneVerified;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private Integer totalOrders;
    private BigDecimal totalSpent;
    private String notes;
    private Integer role; // Thêm role field

    // Constructors
    public UserUpdateDTO() {}

    public UserUpdateDTO(Integer id, String fullName, String phone, String email, 
                        Boolean emailVerified, Boolean phoneVerified, LocalDate dateOfBirth, 
                        String gender, String address, Integer totalOrders, BigDecimal totalSpent, 
                        String notes) {
        this.id = id;
        // this.account = account; // Không set account để tránh mất liên kết
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.emailVerified = emailVerified;
        this.phoneVerified = phoneVerified;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.totalOrders = totalOrders;
        this.totalSpent = totalSpent;
        this.notes = notes;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    // public Integer getAccount() { return account; }
    // public void setAccount(Integer account) { this.account = account; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getEmailVerified() { return emailVerified; }
    public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }

    public Boolean getPhoneVerified() { return phoneVerified; }
    public void setPhoneVerified(Boolean phoneVerified) { this.phoneVerified = phoneVerified; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getTotalOrders() { return totalOrders; }
    public void setTotalOrders(Integer totalOrders) { this.totalOrders = totalOrders; }

    public BigDecimal getTotalSpent() { return totalSpent; }
    public void setTotalSpent(BigDecimal totalSpent) { this.totalSpent = totalSpent; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Integer getRole() { return role; }
    public void setRole(Integer role) { this.role = role; }
}
