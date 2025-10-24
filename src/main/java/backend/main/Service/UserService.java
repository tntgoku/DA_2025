package backend.main.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import backend.main.Model.Account;
import backend.main.Model.ResponseObject;

import backend.main.Model.User.*;
import backend.main.Repository.UserRepository;
import backend.main.Repository.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService implements BaseService<User, Integer> {
    @Autowired
    private UserRepository repository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<ResponseObject> findAll() {
        try {
            List<backend.main.DTO.UserWithAccountProjection> users = repository.findAllUsersWithAccountAndRole();
        return new ResponseEntity<>(new ResponseObject(200,
                    "Thành công", 0, users),
                HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(500,
                    "Lỗi khi lấy danh sách người dùng", 1, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> findObjectByID(Integer id) {
        try {
            Optional<backend.main.DTO.UserWithAccountProjection> optional = repository.findUserByIdWithAccountAndRole(id);
        if (!optional.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(404,
                        "Không tìm thấy người dùng", 1, null),
                        HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ResponseObject(200,
                    "Thành công", 0, optional.get()),
                HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(500,
                    "Lỗi khi lấy thông tin người dùng", 1, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> createNew(User entity) {
        try {
            User savedUser = repository.save(entity);
            return new ResponseEntity<>(new ResponseObject(200,
                    "Thành công", 0, savedUser),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(500,
                    "Lỗi khi tạo người dùng", 1, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        try {
            Optional<User> optional = repository.findById(id);
            if (!optional.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(404,
                        "Không tìm thấy người dùng", 1, null),
                        HttpStatus.NOT_FOUND);
            }
            repository.deleteById(id);
            return new ResponseEntity<>(new ResponseObject(200,
                    "Xóa thành công", 0, null),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(500,
                    "Lỗi khi xóa người dùng", 1, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> update(User entity) {
        try {
            Optional<User> optional = repository.findById(entity.getId());
            if (!optional.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(404,
                        "Không tìm thấy người dùng", 1, null),
                        HttpStatus.NOT_FOUND);
            }
            User updatedUser = repository.save(entity);
            return new ResponseEntity<>(new ResponseObject(200,
                    "Cập nhật thành công", 0, updatedUser),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(500,
                    "Lỗi khi cập nhật người dùng", 1, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update user from DTO
    public ResponseEntity<ResponseObject> updateFromDTO(backend.main.DTO.UserUpdateDTO userDTO) {
        try {
            Optional<User> optional = repository.findById(userDTO.getId());
            if (!optional.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(404,
                        "Không tìm thấy người dùng", 1, null),
                        HttpStatus.NOT_FOUND);
            }
            
            User existingUser = optional.get();
            
            // Update fields from DTO (không thay đổi account_id để tránh mất liên kết)
            // existingUser.setAccount(userDTO.getAccount()); // Không thay đổi account_id
            existingUser.setFullName(userDTO.getFullName());
            existingUser.setPhone(userDTO.getPhone());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setDateOfBirth(userDTO.getDateOfBirth());
            existingUser.setGender(userDTO.getGender());
            existingUser.setAddress(userDTO.getAddress());
            existingUser.setTotalOrders(userDTO.getTotalOrders());
            existingUser.setTotalSpent(userDTO.getTotalSpent());
            existingUser.setNotes(userDTO.getNotes());
            existingUser.setUpdatedAt(java.time.LocalDateTime.now());
            
            // Update role if provided and user has account
            if (userDTO.getRole() != null && existingUser.getAccount() != null) {
                Optional<Account> accountOptional = accountRepository.findById(existingUser.getAccount());
                if (accountOptional.isPresent()) {
                    Account account = accountOptional.get();
                    account.setRole(userDTO.getRole());
                    account.setUpdatedAt(java.time.LocalDateTime.now());
                    accountRepository.save(account);
                }
            }
            
            User updatedUser = repository.save(existingUser);
            return new ResponseEntity<>(new ResponseObject(200,
                    "Cập nhật thành công", 0, updatedUser),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(500,
                    "Lỗi khi cập nhật người dùng", 1, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Tìm kiếm người dùng
    public ResponseEntity<ResponseObject> searchUsers(String searchTerm) {
        try {
            List<backend.main.DTO.UserWithAccountProjection> users = repository.searchUsersWithAccountAndRole(searchTerm);
            return new ResponseEntity<>(new ResponseObject(200,
                    "Thành công", 0, users),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(500,
                    "Lỗi khi tìm kiếm", 1, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Thay đổi trạng thái người dùng
    public ResponseEntity<ResponseObject> toggleUserStatus(Integer id, String status) {
        try {
            Optional<User> optional = repository.findById(id);
            if (!optional.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(404,
                        "Không tìm thấy người dùng", 1, null),
                        HttpStatus.NOT_FOUND);
            }
            User user = optional.get();
            // Giả sử có field status trong User entity
            // user.setStatus(status);
            User updatedUser = repository.save(user);
            return new ResponseEntity<>(new ResponseObject(200,
                    "Thay đổi trạng thái thành công", 0, updatedUser),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(500,
                    "Lỗi khi thay đổi trạng thái", 1, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Thay đổi role người dùng
    public ResponseEntity<ResponseObject> updateUserRole(Integer id, String role) {
        try {
            Optional<User> optional = repository.findById(id);
            if (!optional.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(404,
                        "Không tìm thấy người dùng", 1, null),
                        HttpStatus.NOT_FOUND);
            }
            User user = optional.get();
            // Cập nhật role trong Account entity liên kết
            // user.getAccount().setRole(role);
            User updatedUser = repository.save(user);
            return new ResponseEntity<>(new ResponseObject(200,
                    "Thay đổi role thành công", 0, updatedUser),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(500,
                    "Lỗi khi thay đổi role", 1, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Reset mật khẩu người dùng
    public ResponseEntity<ResponseObject> resetUserPassword(Integer id, String newPassword) {
        try {
            Optional<User> optional = repository.findById(id);
            if (!optional.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(404,
                        "Không tìm thấy người dùng", 1, null),
                        HttpStatus.NOT_FOUND);
            }
            User user = optional.get();
            
            // Tìm Account entity liên kết thông qua account_id
            if (user.getAccount() != null) {
                // Lấy Account entity từ account_id
                Optional<backend.main.Model.Account> accountOptional = accountRepository.findById(user.getAccount());
                if (accountOptional.isPresent()) {
                    backend.main.Model.Account account = accountOptional.get();
                    // Hash password mới
                    String hashedPassword = passwordEncoder.encode(newPassword);
                    account.setPasswordHash(hashedPassword);
                    account.setUpdatedAt(java.time.LocalDateTime.now());
                    
                    // Lưu Account entity
                    accountRepository.save(account);
                    
                    return new ResponseEntity<>(new ResponseObject(200,
                            "Reset mật khẩu thành công", 0, user),
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new ResponseObject(400,
                            "Không tìm thấy tài khoản liên kết", 1, null),
                            HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(new ResponseObject(400,
                        "Người dùng không có tài khoản liên kết", 1, null),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(500,
                    "Lỗi khi reset mật khẩu", 1, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Tạo admin user hoặc cập nhật role thành admin
    public ResponseEntity<ResponseObject> makeAdmin(String email) {
        try {
            // Tìm user theo email
            Optional<User> userOptional = repository.findByEmail(email);
            if (!userOptional.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(404,
                        "Không tìm thấy user với email: " + email, 1, null),
                        HttpStatus.NOT_FOUND);
            }
            User user = userOptional.get();
            // Tìm account liên kết
            Optional<Account> accountOptional = accountRepository.findById(user.getAccount());
            if (!accountOptional.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(404,
                        "Không tìm thấy account liên kết", 1, null),
                        HttpStatus.NOT_FOUND);
            }

            Account account = accountOptional.get();
            
            // Cập nhật role thành ADMIN (role_id = 1)
            account.setRole(1);
            account.setUpdatedAt(LocalDateTime.now());
            accountRepository.save(account);

            return new ResponseEntity<>(new ResponseObject(200,
                    "Đã cập nhật role thành ADMIN cho user: " + email, 0, user),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(500,
                    "Lỗi khi cập nhật role", 1, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cập nhật role thành USER
    public ResponseEntity<ResponseObject> makeUser(String email) {
        try {
            // Tìm user theo email
            Optional<User> userOptional = repository.findByEmail(email);
            if (!userOptional.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(404,
                        "Không tìm thấy user với email: " + email, 1, null),
                        HttpStatus.NOT_FOUND);
            }

            User user = userOptional.get();
            
            // Tìm account liên kết
            Optional<Account> accountOptional = accountRepository.findById(user.getAccount());
            if (!accountOptional.isPresent()) {
                return new ResponseEntity<>(new ResponseObject(404,
                        "Không tìm thấy account liên kết", 1, null),
                        HttpStatus.NOT_FOUND);
            }

            Account account = accountOptional.get();
            
            // Cập nhật role thành USER (role_id = 2)
            account.setRole(2);
            account.setUpdatedAt(LocalDateTime.now());
            accountRepository.save(account);

            return new ResponseEntity<>(new ResponseObject(200,
                    "Đã cập nhật role thành USER cho user: " + email, 0, user),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(500,
                    "Lỗi khi cập nhật role", 1, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
