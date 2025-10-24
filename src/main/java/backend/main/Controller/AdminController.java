package backend.main.Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class AdminController {

    // Cái này không dùng đếnđến
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DeleteMapping("/clear-database")
    public ResponseEntity<String> clearDatabase() {
        try {
            String sql = """
                        ALTER TABLE order_items NOCHECK CONSTRAINT ALL;
                        ALTER TABLE orders NOCHECK CONSTRAINT ALL;
                        ALTER TABLE vouchers NOCHECK CONSTRAINT ALL;
                        ALTER TABLE users NOCHECK CONSTRAINT ALL;

                        DELETE FROM order_items;
                        DELETE FROM orders;
                        DELETE FROM vouchers;
                        DELETE FROM users;

                        ALTER TABLE order_items WITH CHECK CHECK CONSTRAINT ALL;
                        ALTER TABLE orders WITH CHECK CHECK CONSTRAINT ALL;
                        ALTER TABLE vouchers WITH CHECK CHECK CONSTRAINT ALL;
                        ALTER TABLE users WITH CHECK CHECK CONSTRAINT ALL;
                    """;

            jdbcTemplate.execute(sql);
            return ResponseEntity.ok("Database cleared successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error clearing database: " + e.getMessage());
        }
    }
}
