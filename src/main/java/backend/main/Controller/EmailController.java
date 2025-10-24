// package backend.main.Controller;

// import org.springframework.http.ResponseEntity;
// import org.springframework.lang.NonNull;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;
// import backend.main.Service.EmailService;
// import backend.main.Service.ServiceImp.Product.ProductService;

// import java.util.*;

// import backend.main.DTO.Product.ProductDTO;
// import backend.main.Model.ResponseObject;

// @RestController
// public class EmailController {
// private final EmailService emailService;
// private final ProductService service;

// public EmailController(EmailService emailService, ProductService service) {
// this.emailService = emailService;
// this.service = service;
// }

// @GetMapping("/send-email")
// public String sendEmail() {
// try {
// // Lấy ResponseEntity<ResponseObject> từ service
// ResponseEntity<ResponseObject> response = service.findAll();
// // Lấy dữ liệu từ body và ép kiểu an toàn
// Object data = response.getBody().getData();
// if (data instanceof List) {
// @SuppressWarnings("unchecked")
// List<ProductDTO> productList = (List<ProductDTO>) data;
// String htmlContent = buildProductCardsEmail(productList);
// emailService.sendHtmlEmail(
// "lehungqwessar1dd@gmail.com",
// "Test Email",
// htmlContent);
// } else {
// // Xử lý trường hợp dữ liệu không phải là List
// return "Error: Data is not a list";
// }
// } catch (Exception e) {
// e.printStackTrace();
// return "Error: " + e.getMessage();
// }
// return "Email sent!";
// }

// public String buildProductCardsEmail(List<ProductDTO> products) {
// StringBuilder html = new StringBuilder();
// html.append("""
// <div style="font-family: Arial, sans-serif; max-width: 600px;">
// <h2 style="color: #2c3e50;">🎁 Danh sách sản phẩm</h2>
// """);

// for (ProductDTO product : products) {
// // Kiểm tra xem có variants không và ít nhất một phần tử
// String storageInfo = "Không có thông tin";
// if (product.getVariants() != null && !product.getVariants().isEmpty()) {
// storageInfo = product.getVariants().get(0).getStorage();
// }
// html.append("""
// <div style="border: 1px solid #ddd; padding: 15px; margin: 10px 0;
// border-radius: 8px;">
// <h3 style="color: #e74c3c; margin-top: 0;">%s</h3>
// <p><strong>Giá:</strong> %s VND</p>
// <p>%s</p>
// </div>
// """.formatted(product.getName(), storageInfo, product.getDescription()));
// }

// html.append("""
// <p style="color: #7f8c8d;"><i>Cảm ơn bạn đã quan tâm!</i></p>
// </div>
// """);

// return html.toString();
// }
// }