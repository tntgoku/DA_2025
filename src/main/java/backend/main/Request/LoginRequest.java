
package backend.main.Request;

public class LoginRequest {

    private String username;
    private String passwordHash;
    private String token;
    public LoginRequest() {
    }
    public LoginRequest(String username, String passwordHash, String token) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.token = token;
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
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    @Override
    public String toString() {
        return "LoginRequest [username=" + username + ", passwordHash=" + passwordHash + ", token=" + token + "]";
    }

   
}