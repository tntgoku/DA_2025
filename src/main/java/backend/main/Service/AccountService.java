package backend.main.Service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import backend.main.Model.Account;
import backend.main.Model.ResponseObject;
import backend.main.Repository.AccountRepository;

@Service
public class AccountService implements BaseService<Account, Integer> {
    private AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<ResponseObject> checkLogin(String user, String password) {
        Optional<Account> account = repository.login(user, password);

        if (!account.isPresent()) {
            return new ResponseEntity<>(
                    new ResponseObject(401, "Tài khoản hoặc mật khẩu không đúng.", 0, null),
                    HttpStatus.UNAUTHORIZED // Explicitly set the HTTP status code
            );
        }

        return new ResponseEntity<>(
                new ResponseObject(200, "Đăng nhập thành công.", 1, account.get()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseObject> createNew(Account entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> update(Account entity) {
        // TODO Auto-generated method stub
        return null;
    }
}
