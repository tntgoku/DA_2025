package backend.main.Request;

import java.time.LocalDateTime;

public class AccountRequest {
    private Integer id;

    private String username;

    private String passwordHash;

    private String role;

    private Boolean isActive = true;

    private LocalDateTime lastLogin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "AccountRequest [id=" + id + ", username=" + username + ", passwordHash=" + passwordHash + ", role="
                + role + ", isActive=" + isActive + ", lastLogin=" + lastLogin + "]";
    }

    public AccountRequest() {
    }

}
