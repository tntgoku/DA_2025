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

        @Query("SELECT u.id as idUser, u.fullName as fullName, u.email as email, u.phone as phone, ac.passwordHash as passwordHash, r.roleName as roleName "
                        + "FROM User u JOIN Account ac ON u.account = ac.id "
                        + "JOIN Role r ON ac.role = r.id "
                        + "WHERE u.email = :email and ac.username = :email")
        Optional<AuthzProjection> findByEmailWithAccountAndRole(@Param("email") String email);

        @Query("SELECT u.id as idUser, u.fullName as fullName, u.email as email, u.phone as phone, ac.passwordHash as passwordHash, r.roleName as roleName "
                        + "FROM User u JOIN Account ac ON u.account = ac.id "
                        + "JOIN Role r ON ac.role = r.id "
                        + "WHERE u.email = :email")
        Optional<AuthzProjection> findByPhoneWithAccountAndRole(@Param("email") String email);

        @Query("""
                            SELECT u.id as idUser, u.fullName as fullName, u.email as email,
                                   u.phone as phone,
                                   ac.passwordHash as passwordHash, r.roleName as roleName
                            FROM User u
                            JOIN Account ac ON u.account = ac.id
                            JOIN Role r ON ac.role = r.id
                            WHERE u.email = :email OR u.phone = :phone
                        """)
        Optional<AuthzProjection> findByEmailOrPhoneWithAccountAndRole(@Param("email") String email,
                        @Param("phone") String phone);

        // Tìm kiếm người dùng theo tên, email hoặc số điện thoại
        @Query("SELECT u FROM User u WHERE " +
               "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
               "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
               "u.phone LIKE CONCAT('%', :searchTerm, '%')")
        List<User> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContaining(
            @Param("searchTerm") String searchTerm1,
            @Param("searchTerm") String searchTerm2,
            @Param("searchTerm") String searchTerm3
        );

        // Lấy tất cả User với thông tin Account và Role
        @Query("SELECT u.id as idUser, u.account as accountId, u.fullName as fullName, " +
               "u.phone as phone, u.email as email, " +
               "u.dateOfBirth as dateOfBirth, " +
               "u.gender as gender, u.address as address, u.totalOrders as totalOrders, " +
               "u.totalSpent as totalSpent, u.notes as notes, u.createdAt as createdAt, " +
               "u.updatedAt as updatedAt, ac.username as username, r.roleName as roleName, " +
               "r.id as roleId, ac.isActive as status " +
               "FROM User u " +
               "LEFT JOIN Account ac ON u.account = ac.id " +
               "LEFT JOIN Role r ON ac.role = r.id")
        List<backend.main.DTO.UserWithAccountProjection> findAllUsersWithAccountAndRole();

        // Tìm kiếm User với thông tin Account và Role
        @Query("SELECT u.id as idUser, u.account as accountId, u.fullName as fullName, " +
               "u.phone as phone, u.email as email, " +
               "u.dateOfBirth as dateOfBirth, " +
               "u.gender as gender, u.address as address, u.totalOrders as totalOrders, " +
               "u.totalSpent as totalSpent, u.notes as notes, u.createdAt as createdAt, " +
               "u.updatedAt as updatedAt, ac.username as username, r.roleName as roleName, " +
               "r.id as roleId, ac.isActive as status " +
               "FROM User u " +
               "LEFT JOIN Account ac ON u.account = ac.id " +
               "LEFT JOIN Role r ON ac.role = r.id " +
               "WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
               "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
               "u.phone LIKE CONCAT('%', :searchTerm, '%')")
        List<backend.main.DTO.UserWithAccountProjection> searchUsersWithAccountAndRole(@Param("searchTerm") String searchTerm);

        // Lấy User theo ID với thông tin Account và Role
        @Query("SELECT u.id as idUser, u.account as accountId, u.fullName as fullName, " +
               "u.phone as phone, u.email as email, " +
               "u.dateOfBirth as dateOfBirth, " +
               "u.gender as gender, u.address as address, u.totalOrders as totalOrders, " +
               "u.totalSpent as totalSpent, u.notes as notes, u.createdAt as createdAt, " +
               "u.updatedAt as updatedAt, ac.username as username, r.roleName as roleName, " +
               "r.id as roleId, ac.isActive as status " +
               "FROM User u " +
               "LEFT JOIN Account ac ON u.account = ac.id " +
               "LEFT JOIN Role r ON ac.role = r.id " +
               "WHERE u.id = :id")
        Optional<backend.main.DTO.UserWithAccountProjection> findUserByIdWithAccountAndRole(@Param("id") Integer id);
}
