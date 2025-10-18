package backend.main.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.Model.ResponseObject;
import backend.main.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PatchMapping;
import java.util.*;
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<ResponseObject> getMethodName() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getMethodNameByID(@PathVariable String id) {
        Integer iduser = Integer.parseInt(id);
        return service.findObjectByID(iduser);
    }

    @PostMapping
    public ResponseEntity<ResponseObject> createUser(@RequestBody backend.main.Model.User.User user) {
        return service.createNew(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateUser(@PathVariable Integer id, @RequestBody backend.main.DTO.UserUpdateDTO userDTO) {
        userDTO.setId(id);
        return service.updateFromDTO(userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable Integer id) {
        return service.delete(id);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject> searchUsers(@RequestParam String q) {
        return service.searchUsers(q);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseObject> toggleUserStatus(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        return service.toggleUserStatus(id, status);
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<ResponseObject> updateUserRole(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        String role = request.get("role");
        return service.updateUserRole(id, role);
    }

    @PatchMapping("/{id}/reset-password")
    public ResponseEntity<ResponseObject> resetUserPassword(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        String newPassword = request.get("newPassword");
        return service.resetUserPassword(id, newPassword);
    }

    // Endpoint đặc biệt để tạo admin user hoặc cập nhật role thành admin
    @PostMapping("/make-admin")
    public ResponseEntity<ResponseObject> makeAdmin(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        return service.makeAdmin(email);
    }

    // Endpoint để cập nhật role thành user
    @PostMapping("/make-user")
    public ResponseEntity<ResponseObject> makeUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        return service.makeUser(email);
    }
}
