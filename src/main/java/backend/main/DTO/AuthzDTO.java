package backend.main.DTO;

import lombok.Data;
import java.util.*;
import backend.main.Model.Order.*;;

@Data
public class AuthzDTO {
    private Integer idUser;
    private String fullName;
    private boolean emailVerified;
    private String email;
    private String phone;
    private boolean phoneVerified;
    private String passwordHash;
    private String roleName;
    private List<OrderDTO> listorder;

    public AuthzDTO() {

    }

    public Integer getIdUser() {
        return this.idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isEmailVerified() {
        return this.emailVerified;
    }

    public boolean getEmailVerified() {
        return this.emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPhoneVerified() {
        return this.phoneVerified;
    }

    public boolean getPhoneVerified() {
        return this.phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<OrderDTO> getListorder() {
        return this.listorder;
    }

    public void setListorder(List<OrderDTO> listorder) {
        this.listorder = listorder;
    }

    public AuthzDTO(Integer idUser, String fullName, boolean emailVerified, String email, String phone,
            boolean phoneVerified, String passwordHash, String roleName) {
        this.idUser = idUser;
        this.fullName = fullName;
        this.emailVerified = emailVerified;
        this.email = email;
        this.phone = phone;
        this.phoneVerified = phoneVerified;
        this.passwordHash = passwordHash;
        this.roleName = roleName;
    }
}
