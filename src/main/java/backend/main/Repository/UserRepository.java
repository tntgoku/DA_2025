package backend.main.Repository;

import backend.main.DTO.AuthzProjection;
import backend.main.Model.User.User;
import java.util.*;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, Integer> {
        @EntityGraph(attributePaths = { "orders", "orders.orderItems" })
        List<User> findAll();

        @Query("SELECT u FROM User u WHERE u.email = :email")
        public java.util.Optional<User> findByEmail(String email);

        @Query("SELECT u FROM User u WHERE u.phone = :phone")
        public Optional<User> findByPhone(String phone);

        // Assuming your DTO is in com.yourpackage.LoginResponseDTO
        @Query("SELECT u.id as idUser, u.fullName as fullName, u.emailVerified as emailVerified, u.email as email, u.phone as phone, u.phoneVerified as phoneVerified, ac.passwordHash as passwordHash, r.roleName as roleName "
                        + "FROM User u JOIN Account ac ON u.account = ac.id "
                        + "JOIN Role r ON ac.role = r.id "
                        + "WHERE u.email = :email AND ac.passwordHash = :pass")
        Optional<AuthzProjection> checklogin(@Param("email") String email, @Param("pass") String pass);

        @Query("SELECT u.id as idUser, u.fullName as fullName, u.emailVerified as emailVerified, u.email as email, u.phone as phone, u.phoneVerified as phoneVerified, ac.passwordHash as passwordHash, r.roleName as roleName "
                        + "FROM User u JOIN Account ac ON u.account = ac.id "
                        + "JOIN Role r ON ac.role = r.id "
                        + "WHERE u.email = :email")
        Optional<AuthzProjection> findByEmailProjection(@Param("email") String email);
}
