package backend.main.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.main.DTO.AuthzProjection;
import backend.main.Repository.UserRepository;

@Service
public class AuthzService {
    @Autowired
    private UserRepository userRepository;

    public Optional<AuthzProjection> checklogin(String user, String password) {
        return userRepository.checklogin(user, password);
    }
}
