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

        Optional<User> findByEmail(String email);
        Optional<User> findByPhone(String phone);

        @Query("SELECT u.id as idUser, u.fullName as fullName, u.emailVerified as emailVerified, u.email as email, u.phone as phone, u.phoneVerified as phoneVerified, ac.passwordHash as passwordHash, r.roleName as roleName "
                        + "FROM User u JOIN Account ac ON u.account = ac.id "
                        + "JOIN Role r ON ac.role = r.id "
                        + "WHERE u.email = :email")
        Optional<AuthzProjection> findByEmailWithAccountAndRole(@Param("email") String email);

        @Query("SELECT u.id as idUser, u.fullName as fullName, u.emailVerified as emailVerified, u.email as email, u.phone as phone, u.phoneVerified as phoneVerified, ac.passwordHash as passwordHash, r.roleName as roleName "
                        + "FROM User u JOIN Account ac ON u.account = ac.id "
                        + "JOIN Role r ON ac.role = r.id "
                        + "WHERE u.email = :email")
        Optional<AuthzProjection> findByPhoneWithAccountAndRole(@Param("email") String email);

        @Query("""
                            SELECT u.id as idUser, u.fullName as fullName, u.emailVerified as emailVerified,
                                   u.email as email, u.phone as phone, u.phoneVerified as phoneVerified,
                                   ac.passwordHash as passwordHash, r.roleName as roleName
                            FROM User u
                            JOIN Account ac ON u.account = ac.id
                            JOIN Role r ON ac.role = r.id
                            WHERE u.email = :email OR u.phone = :phone
                        """)
        Optional<AuthzProjection> findByEmailOrPhoneWithAccountAndRole(@Param("email") String email,
                        @Param("phone") String phone);

        // @Query("SELECT u.id as idUser, u.fullName as fullName, u.emailVerified as
        // emailVerified, u.email as email, u.phone as phone, u.phoneVerified as
        // phoneVerified, ac.passwordHash as passwordHash, r.roleName as roleName "
        // + "FROM User u JOIN Account ac ON u.account = ac.id "
        // + "JOIN Role r ON ac.role = r.id "
        // + "WHERE u.email = :email")
        // Optional<AuthzProjection> findByEmailProjection(@Param("email") String
        // email);
}
