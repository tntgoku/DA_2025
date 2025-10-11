package backend.main.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import backend.main.Model.ResponseObject;

import backend.main.Model.User.*;
import backend.main.Repository.UserRepository;

@Service
public class UserService implements BaseService<User, Integer> {
    @Autowired
    private UserRepository repository;

    public ResponseEntity<ResponseObject> findAll() {
        return new ResponseEntity<>(new ResponseObject(200,
                "Thành công", 0, repository.findAll()),
                HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> findObjectByID(Integer id) {
        Optional<User> optional = repository.findById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(new ResponseObject(4004,
                    "Không thành công", 0, null),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseObject(200,
                "Thành công", 0, repository.findById(id)),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseObject> createNew(User entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> update(User entity) {
        // TODO Auto-generated method stub
        return null;
    }

}
