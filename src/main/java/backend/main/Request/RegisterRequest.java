package backend.main.Request;

public class RegisterRequest {
    private String name;
    private String phone;
    private String email;
    private String password;
    private String confirmPassword;

    @Override
    public String toString() {
        return "RegisterRequest [name=" + name + ", phone=" + phone + ", email=" + email + ", password=" + password
                + ", confirmPassword=" + confirmPassword + "]";
    }

    public RegisterRequest(String name, String phone, String email, String password, String confirmPassword) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public RegisterRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
