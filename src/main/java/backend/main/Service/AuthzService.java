package backend.main.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import backend.main.Config.LoggerE;
import backend.main.DTO.*;
import backend.main.Jwt.JwtUtil;
import backend.main.Model.Account;
import backend.main.Model.ResponseObject;
import backend.main.Model.User.User;
import backend.main.Repository.AccountRepository;
import backend.main.Repository.OrderRepository;
import backend.main.Repository.UserRepository;
import backend.main.Request.RegisterRequest;
import java.util.*;

@Service
public class AuthzService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final OrderService orderService;
    private final JwtUtil jwtUtil;
    private final Logger logger = LoggerE.logger;

    public AuthzService(JwtUtil jwtUtil, PasswordEncoder password, UserRepository userRepository,
            AccountRepository accountRepository, OrderService orderService) {
        this.passwordEncoder = password;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.orderService = orderService;
    }

    public ResponseEntity<ResponseObject> checklogin(String user, String password) {
        Optional<AuthzProjection> userOptional = userRepository.findByEmailWithAccountAndRole(user);
        if (userOptional.isEmpty()) {
            logger.info("Tài khoản " + user + " không tồn tại");
            return new ResponseEntity<>(
                    new ResponseObject(401, "Tài khoản không tồn tại", 401, "Tài khoản không tồn tại"),
                    HttpStatus.UNAUTHORIZED);
        }
        if (!passwordEncoder.matches(password, userOptional.get().getPasswordHash())) {
            logger.info("Tài khoản hoặc mật khẩu không chính xác \n User: " + user + ",Password: " + password);
            return new ResponseEntity<>(
                    new ResponseObject(401, "Tài khoản hoặc mật khẩu không chính xác", 401,
                            "Tài khoản hoặc mật khẩu không chính xác"),
                    HttpStatus.OK);
        }
        // String tokeString=jwtUtil.generateToken(null);
        logger.info("Đăng nhập thành công");
        logger.info("Infor User: " + userOptional.get().toString());
        HashMap<String, String> mHashMap = new HashMap<>();
        Optional<User> userlogin = userRepository.findById(userOptional.get().getIdUser());
        String tokengenerate = jwtUtil.generateToken(userlogin.get());
        mHashMap.put("token", tokengenerate);
        mHashMap.put("name", userOptional.get().getFullName());
        return new ResponseEntity<>(
                new ResponseObject(200, "Đăng nhập thành công", 0, mHashMap),
                HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> checkRegister(RegisterRequest request) {
        Optional<AuthzProjection> userOptional = userRepository.findByEmailOrPhoneWithAccountAndRole(request.getEmail(),
                request.getPhone());

        if (userOptional.isPresent()) {
            AuthzProjection user = userOptional.get();

            if (request.getEmail().equalsIgnoreCase(user.getEmail())) {
                return ResponseEntity.ok(
                        new ResponseObject(200, "Thất bại", 1, "Email này đã tồn tại"));
            }

            if (request.getPhone().equals(user.getPhone())) {
                return ResponseEntity.ok(
                        new ResponseObject(200, "Thất bại", 1, "Số điện thoại này đã tồn tại"));
            }
        }
        Account account = new Account();
        account.setUsername(request.getEmail());
        account.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        // Role_id NameRule(Description)
        // 1 Admin Quản trị viên tối cao
        // 2 Staff Nhân viên bán hàng/kho hàng
        // 3 Customer Khách hàng thông thường
        account.setRole(3); // 3 = user thường
        Account newacc = accountRepository.save(account);

        // Lưu user (liên kết với account vừa tạo)
        User user = new User();
        user.setFullName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setGender("");
        user.setAccount(newacc.getId());
        User newus = userRepository.save(user);

        Optional<AuthzProjection> userOptional1 = userRepository.findByEmailWithAccountAndRole(newus.getEmail());
        return ResponseEntity.ok(
                new ResponseObject(200, "Thành công", 1, userOptional1.get().getFullName()));
    }

    public ResponseEntity<ResponseObject> checkProfile(String email) {
        Optional<AuthzProjection> userOpt = userRepository.findByEmailWithAccountAndRole(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject(404, "Không tìm thấy người dùng", 404, null));
        }

        AuthzProjection user = userOpt.get();
        AuthzDTO audto = ConvertDTO(user);
        return ResponseEntity.ok(new ResponseObject(200, "Thành công", 1, audto));
    }

    public AuthzDTO ConvertDTO(AuthzProjection authzProjection) {
        AuthzDTO a = new AuthzDTO();
        a.setIdUser(authzProjection.getIdUser());
        a.setEmail(authzProjection.getEmail());
        a.setEmailVerified(authzProjection.getEmailVerified());
        a.setFullName(authzProjection.getFullName());
        a.setPhone(authzProjection.getPhone());
        a.setPhoneVerified(authzProjection.getPhoneVerified());
        a.setPasswordHash(authzProjection.getPasswordHash());
        a.setRoleName(authzProjection.getRoleName());
        List<OrderDTO> orders = (List<OrderDTO>) orderService.findAllOrderByIdUser(a.getIdUser()).getBody().getData();
        a.setListorder(orders);
        return a;
    }
}
