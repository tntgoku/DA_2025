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
import backend.main.Model.ResponseObject;
import backend.main.Request.AccountRequest;
import backend.main.Service.AuthzService;

@RestController
@RequestMapping("/api/authz")
public class AuthenticationController {
    private final Logger logger = LoggerE.logger;
    @Autowired
    private AuthzService authzService;

    @PostMapping
    public ResponseEntity<ResponseObject> checklogin(@RequestBody AccountRequest data) {

        logger.info(data.toString());
        return new ResponseEntity<>(new ResponseObject(200, null, 0,
                authzService.checklogin(data.getUsername(), data.getPasswordHash()).get()), HttpStatus.OK);
    }

}
