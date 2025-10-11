package backend.main.DTO;

public interface AuthzProjection {
    Integer getIdUser();

    String getFullName();

    Boolean getEmailVerified();

    String getEmail();

    String getPhone();

    Boolean getPhoneVerified();

    String getPasswordHash();

    String getRoleName();
}
