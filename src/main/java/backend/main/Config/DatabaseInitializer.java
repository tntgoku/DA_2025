// package backend.main.Config;

// import jakarta.annotation.PostConstruct;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.stereotype.Component;

// @Component
// public class DatabaseInitializer {

// @Autowired
// private JdbcTemplate jdbcTemplate;

// @PostConstruct
// public void checkAndFixSpecificationsColumn() {
// try {
// // Kiểm tra kiểu dữ liệu hiện tại của cột specifications
// String checkQuery = """
// SELECT DATA_TYPE, CHARACTER_MAXIMUM_LENGTH
// FROM INFORMATION_SCHEMA.COLUMNS
// WHERE TABLE_NAME = 'products' AND COLUMN_NAME = 'specifications';
// """;

// jdbcTemplate.query(checkQuery, rs -> {
// String dataType = rs.getString("DATA_TYPE");
// int maxLength = rs.getInt("CHARACTER_MAXIMUM_LENGTH");

// System.out.println("🔍 Kiểm tra cột 'specifications': " + dataType + "(" +
// maxLength + ")");

// // Nếu độ dài nhỏ hơn 4000 hoặc kiểu không phải nvarchar => thay đổi
// if (!dataType.equalsIgnoreCase("nvarchar") || maxLength < 4000) {
// System.out.println("⚙️ Cập nhật cột 'specifications' thành
// NVARCHAR(MAX)...");
// jdbcTemplate.execute("""
// ALTER TABLE products
// ALTER COLUMN specifications NVARCHAR(MAX);
// """);
// System.out.println("✅ Đã cập nhật xong!");
// } else {
// System.out.println("✅ Cột 'specifications' đã đúng cấu hình.");
// }
// });

// } catch (Exception e) {
// System.err.println("❌ Lỗi khi kiểm tra/cập nhật cột specifications: " +
// e.getMessage());
// }
// }
// }
