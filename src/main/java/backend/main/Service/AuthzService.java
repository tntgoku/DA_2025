package backend.main.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
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
import backend.main.Repository.UserRepository;
import backend.main.Request.RegisterRequest;
import backend.main.Request.ForgotPasswordRequest;
import backend.main.Request.ResetPasswordRequest;
import java.util.*;

@Service
public class AuthzService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final OrderService orderService;
    private final JwtUtil jwtUtil;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AuthzService.class);

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
            logger.info("Tài khoản {} không tồn tại", user);
            return new ResponseEntity<>(
                    new ResponseObject(401, "Tài khoản không tồn tại", 401, "Tài khoản không tồn tại"),
                    HttpStatus.UNAUTHORIZED);
        }
        if (!passwordEncoder.matches(password, userOptional.get().getPasswordHash())) {
            logger.info("Tài khoản hoặc mật khẩu không chính xác \n User: {},Password: {}", user, password);
            return new ResponseEntity<>(
                    new ResponseObject(401, "Tài khoản hoặc mật khẩu không chính xác", 401,
                            "Tài khoản hoặc mật khẩu không chính xác"),
                    HttpStatus.OK);
        }
        // String tokeString=jwtUtil.generateToken(null);
        logger.info("Đăng nhập thành công");
        logger.info("Infor User: {}", userOptional.get().toString());
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
        @SuppressWarnings("unchecked")
        List<OrderDTO> orders = (List<OrderDTO>) orderService.findAllOrderByIdUser(a.getIdUser()).getBody().getData();
        a.setListorder(orders);
        return a;
    }

    public ResponseEntity<ResponseObject> forgotPassword(ForgotPasswordRequest request) {
        Optional<AuthzProjection> userOptional = userRepository.findByEmailWithAccountAndRole(request.getEmail());
        
        if (userOptional.isEmpty()) {
            logger.info("Email không tồn tại: {}", request.getEmail());
            return ResponseEntity.ok(
                new ResponseObject(404, "Email không tồn tại trong hệ thống", 404, "Email không tồn tại")
            );
        }

        // Trong thực tế, bạn sẽ gửi email reset password ở đây
        // Hiện tại chỉ log thông tin
        logger.info("Yêu cầu reset password cho email: {}", request.getEmail());
        
        return ResponseEntity.ok(
            new ResponseObject(200, "Email reset password đã được gửi", 0, "Vui lòng kiểm tra email để reset mật khẩu")
        );
    }

    public ResponseEntity<ResponseObject> resetPassword(ResetPasswordRequest request) {
        // Kiểm tra token (trong thực tế sẽ validate token từ email)
        if (request.getToken() == null || request.getToken().isEmpty()) {
            return ResponseEntity.ok(
                new ResponseObject(400, "Token không hợp lệ", 400, "Token không hợp lệ")
            );
        }

        // Kiểm tra mật khẩu mới và xác nhận
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.ok(
                new ResponseObject(400, "Mật khẩu xác nhận không khớp", 400, "Mật khẩu xác nhận không khớp")
            );
        }

        // Trong thực tế, bạn sẽ:
        // 1. Validate token từ email
        // 2. Tìm user từ token
        // 3. Cập nhật mật khẩu mới
        
        // Hiện tại chỉ log thông tin
        logger.info("Reset password request với token: {}", request.getToken());
        
        return ResponseEntity.ok(
            new ResponseObject(200, "Mật khẩu đã được reset thành công", 0, "Mật khẩu đã được cập nhật")
        );
    }

    public ResponseEntity<ResponseObject> logout(String email) {
        try {
            // TODO: Implement token blacklisting or session invalidation
            // For now, just log the logout
            logger.info("User logged out: {}", email);
            
            return ResponseEntity.ok(
                new ResponseObject(200, "Đăng xuất thành công", 0, "User đã đăng xuất")
            );
        } catch (Exception e) {
            logger.error("Logout error: {}" , e.getMessage());
            return ResponseEntity.badRequest().body(
                new ResponseObject(400, "Đăng xuất thất bại", 0, "Lỗi: " + e.getMessage())
            );
        }
    }

    public ResponseEntity<ResponseObject> updateProfile(String email, Map<String, String> profileData) {
        try {
            // Tìm user theo email từ token
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (!userOptional.isPresent()) {
                return ResponseEntity.badRequest().body(
                    new ResponseObject(400, "Không tìm thấy user", 1, "User không tồn tại")
                );
            }

            User user = userOptional.get();
            
            // Kiểm tra nếu có đổi mật khẩu
            if (profileData.containsKey("currentPassword") && profileData.containsKey("newPassword")) {
                // Tìm account liên kết với user
                Optional<Account> accountOptional = accountRepository.findById(user.getAccount());
                if (!accountOptional.isPresent()) {
                    return ResponseEntity.badRequest().body(
                        new ResponseObject(400, "Không tìm thấy account liên kết", 1, "Account không tồn tại")
                    );
                }

                Account account = accountOptional.get();
                
                // Kiểm tra mật khẩu hiện tại
                String currentPassword = profileData.get("currentPassword");
                if (!passwordEncoder.matches(currentPassword, account.getPasswordHash())) {
                    return ResponseEntity.badRequest().body(
                        new ResponseObject(400, "Mật khẩu hiện tại không đúng", 1, "Vui lòng kiểm tra lại mật khẩu")
                    );
                }
                
                // Cập nhật mật khẩu mới
                String newPassword = profileData.get("newPassword");
                account.setPasswordHash(passwordEncoder.encode(newPassword));
                account.setUpdatedAt(LocalDateTime.now());
                accountRepository.save(account);
                
                logger.info("Password updated for user: {}", email);
                return ResponseEntity.ok(
                    new ResponseObject(200, "Đổi mật khẩu thành công", 0, "Mật khẩu đã được cập nhật")
                );
            }
            
            // Cập nhật thông tin user thông thường
            if (profileData.containsKey("fullName")) {
                user.setFullName(profileData.get("fullName"));
            }
            if (profileData.containsKey("phone")) {
                user.setPhone(profileData.get("phone"));
            }
            if (profileData.containsKey("address")) {
                user.setAddress(profileData.get("address"));
            }
            
            // Cập nhật updatedAt
            user.setUpdatedAt(LocalDateTime.now());
            
            // Lưu user đã cập nhật
            User updatedUser = userRepository.save(user);
            
            logger.info("Profile updated for user: {}", email);
            
            return ResponseEntity.ok(
                new ResponseObject(200, "Cập nhật thông tin thành công", 0, updatedUser)
            );
        } catch (Exception e) {
                logger.error("Update profile error: {}" , e.getMessage());
            return ResponseEntity.badRequest().body(
                new ResponseObject(400, "Cập nhật thông tin thất bại", 1, "Lỗi: {}" + e.getMessage())
            );
        }
    }
}
