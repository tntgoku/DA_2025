package backend.main.DTO;

public interface AuthzProjection {
    Integer getIdUser();

    String getFullName();

    String getEmail();

    String getPhone();

    String getPasswordHash();

    String getRoleName();
}
