package backend.main.DTO;

import lombok.Data;
import java.util.*;

@Data
public class AuthzDTO {
    private Integer idUser;
    private String fullName;
    private String email;
    private String phone;
    private String passwordHash;
    private String roleName;
    private List<OrderDTO> listorder;

    public AuthzDTO() {

    }

    public AuthzDTO(Integer idUser, String fullName, String email, String phone, String passwordHash, String roleName,
            List<OrderDTO> listorder) {
        this.idUser = idUser;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.roleName = roleName;
        this.listorder = listorder;
    }

}
