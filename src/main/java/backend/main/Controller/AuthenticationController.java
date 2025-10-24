package backend.main.Controller;

import java.util.Map;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.Model.Account;
import backend.main.Model.ResponseObject;
import backend.main.Request.LoginRequest;
import backend.main.Request.RegisterRequest;
import backend.main.Request.ForgotPasswordRequest;
import backend.main.Request.ResetPasswordRequest;
import backend.main.Request.ForgotPasswordOtpRequest;
import backend.main.Request.VerifyOtpRequest;
import backend.main.Request.ResetPasswordWithOtpRequest;
import backend.main.Service.AuthzService;
import backend.main.Service.OtpService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/authz")
public class AuthenticationController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthzService authzService;
    
    @Autowired
    private OtpService otpService;

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> checklogin(@RequestBody LoginRequest data) {

        logger.info("Login request: " + data.toString());
        return authzService.checklogin(data.getUsername(), data.getPasswordHash());
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

    // ========== OTP ENDPOINTS ==========
    @PostMapping("/forgot-password-otp")
    public ResponseEntity<ResponseObject> sendForgotPasswordOtp(@RequestBody ForgotPasswordOtpRequest data) {
        if(otpService.findAccount(data.getEmail())){
            logger.info("Send forgot password OTP request: {}", data.toString());
            // return new  ResponseEntity<>(
                // new ResponseObject(200,"Gui thanh cong",0,data),HttpStatus.OK);
                return otpService.generateAndSendForgotPasswordOtp(data.getEmail());
        }
        logger.info("Email này không tồn tại nên ko gửi đc: {}",data.getEmail());
        return new ResponseEntity<>(new ResponseObject(200,"Gui that bai",0,null),HttpStatus.NOT_FOUND);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ResponseObject> verifyOtp(@RequestBody VerifyOtpRequest data) {
        logger.info("Verify OTP request: {}", data.toString());
        //check account
        return otpService.verifyForgotPasswordOtp(data.getEmail(), data.getOtpCode());
    }

    @PostMapping("/reset-password-otp")
    public ResponseEntity<ResponseObject> postMethodName(@RequestBody ResetPasswordWithOtpRequest entity) {
        return otpService.savePassword(entity);
    }
    



}
