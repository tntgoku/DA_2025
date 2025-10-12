package backend.main.Controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.Config.LoggerE;
import backend.main.DTO.AuthzProjection;
import backend.main.Model.ResponseObject;
import backend.main.Request.LoginRequest;
import backend.main.Request.RegisterRequest;
import backend.main.Service.AuthzService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/authz")
public class AuthenticationController {
    private final Logger logger = LoggerE.logger;
    @Autowired
    private AuthzService authzService;

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> checklogin(@RequestBody LoginRequest data) {

        logger.info(data.toString());
        return authzService.checklogin(data.getEmail(), data.getPassword());
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> resgisterAccount(@RequestBody RegisterRequest data) {
        // TODO: process POST request
        logger.info(data.toString());
        return authzService.checkRegister(data);
    }

    @PostMapping("/profile")
    public ResponseEntity<ResponseObject> checkprofile(HttpServletRequest data) {
        String email = (String) data.getAttribute("email");
        logger.info("Email lấy từ token: " + email);
        return authzService.checkProfile(email);
    }

}
