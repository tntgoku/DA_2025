package backend.main.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.main.Model.Account;

@Repository
public interface AccountRepository extends BaseRepository<Account, Integer> {
    @Query("SELECT a FROM Account a WHERE a.username = :username AND a.passwordHash = :passwordHash")
    public Optional<Account> login(@Param("username") String username, @Param("passwordHash") String passwordHash);
}
