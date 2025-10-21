package backend.main.Controller;

import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.Config.LoggerE;
import backend.main.Model.ResponseObject;
import backend.main.Request.LoginRequest;
import backend.main.Request.RegisterRequest;
import backend.main.Request.ForgotPasswordRequest;
import backend.main.Request.ResetPasswordRequest;
import backend.main.Service.AuthzService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/authz")
public class AuthenticationController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthzService authzService;

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> checklogin(@RequestBody LoginRequest data) {

        logger.info("Login request: " + data.toString());
        return authzService.checklogin(data.getEmail(), data.getPassword());
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> resgisterAccount(@RequestBody RegisterRequest data) {
        try {
            logger.info("Register request received: {}", data.toString());
            return authzService.checkRegister(data);
        } catch (Exception e) {
            logger.error("Error in register endpoint: ", e);
            return ResponseEntity.badRequest().body(
                new ResponseObject(400, "Lỗi xử lý đăng ký", 1, e.getMessage())
            );
        }
    }

    @PostMapping("/profile")
    public ResponseEntity<ResponseObject> checkprofile(HttpServletRequest data) {
        String email = (String) data.getAttribute("email");
        logger.info("Email lấy từ token: " + email);
        
        return authzService.checkProfile(email);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseObject> forgotPassword(@RequestBody ForgotPasswordRequest data) {
        logger.info("Forgot password request: " + data.toString());
        return authzService.forgotPassword(data);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseObject> resetPassword(@RequestBody ResetPasswordRequest data) {
        logger.info("Reset password request: " + data.toString());
        return authzService.resetPassword(data);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseObject> logout(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        logger.info("Logout request from: " + email);
        return authzService.logout(email);
    }

    @PutMapping("/profile")
    public ResponseEntity<ResponseObject> updateProfile(HttpServletRequest request, @RequestBody Map<String, String> profileData) {
        String email = (String) request.getAttribute("email");
        logger.info("Update profile request from: " + email);
        return authzService.updateProfile(email, profileData);
    }

}
