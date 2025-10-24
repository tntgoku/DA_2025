package backend.main.Service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import backend.main.Config.LoggerE;

import org.slf4j.Logger;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private static final Logger logger = LoggerE.getLogger();

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendHtmlEmail(String to, String subject, String htmlBody) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(emailFrom);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true); // true để gửi HTML
        mailSender.send(message);
        logger.info("Email sent successfully to: {}", to);
    }

    // Gửi email forgot password
    public void sendForgotPasswordEmail(String to, String resetToken, String userName) throws Exception {
        String resetUrl = frontendUrl + "/reset-password?token=" + resetToken;

        String htmlBody = buildForgotPasswordEmail(userName, resetUrl);
        String subject = "Đặt lại mật khẩu - Trường LCD";

        sendHtmlEmail(to, subject, htmlBody);
    }

    // Gửi email xác nhận đơn hàng
    public void sendOrderConfirmationEmail(String to, String customerName, String orderCode,
            String totalAmount, String orderItems) throws Exception {
        String htmlBody = buildOrderConfirmationEmail(customerName, orderCode, totalAmount, orderItems);
        String subject = "Hóa đơn thanh toán đơn hànghàng " + orderCode + " - Trường LCD";

        sendHtmlEmail(to, subject, htmlBody);
    }

    // Gửi email thông báo cập nhật trạng thái đơn hàng
    public void sendOrderStatusUpdateEmail(String to, String customerName, String orderCode,
            String oldStatus, String newStatus, String notes) throws Exception {
        String htmlBody = buildOrderStatusUpdateEmail(customerName, orderCode, oldStatus, newStatus, notes);
        String subject = "Cập nhật trạng thái đơn hàng #" + orderCode + " - Trường LCD";

        sendHtmlEmail(to, subject, htmlBody);
    }

    public void sendNewCommentNotificationEmail(String adminEmail, String productName, 
    String commenterName, String comment, String productUrl) throws Exception {
    String htmlBody = buildNewCommentNotificationEmail(productName, commenterName, comment, productUrl);
    String subject = "Bình luận mới cho sản phẩm: " + productName;
    sendHtmlEmail(adminEmail, subject, htmlBody);
    }

    // Gửi email OTP
    public void sendOtpEmail(String to, String otpCode, String purpose) throws Exception {
        String htmlBody = buildOtpEmail(otpCode, purpose);
        String subject = "Mã OTP " + purpose + " - Trường LCD";

        sendHtmlEmail(to, subject, htmlBody);
    }

    // Template cho email forgot password
    private String buildForgotPasswordEmail(String userName, String resetUrl) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background: #007bff; color: white; padding: 20px; text-align: center; }
                        .content { padding: 20px; background: #f9f9f9; }
                        .button { display: inline-block; background: #007bff; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                        .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Trường LCD</h1>
                            <p>Đặt lại mật khẩu</p>
                        </div>
                        <div class="content">
                            <h2>Xin chào %s!</h2>
                            <p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.</p>
                            <p>Nhấp vào nút bên dưới để đặt lại mật khẩu:</p>
                            <a href="%s" class="button">Đặt lại mật khẩu</a>
                            <p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>
                            <p><strong>Lưu ý:</strong> Link này sẽ hết hạn sau 24 giờ.</p>
                        </div>
                        <div class="footer">
                            <p>© 2024 Trường LCD. Tất cả quyền được bảo lưu.</p>
                        </div>
                    </div>
                </body>
                </html>
                """
                .formatted(userName, resetUrl);
    }

    // Template cho email xác nhận đơn hàng
    private String buildOrderConfirmationEmail(String customerName, String orderCode,
            String totalAmount, String orderItems) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background: #28a745; color: white; padding: 20px; text-align: center; }
                        .content { padding: 20px; background: #f9f9f9; }
                        .order-info { background: white; padding: 15px; border-radius: 5px; margin: 15px 0; }
                        .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Trường LCD</h1>
                            <p>Hóa đơn thanh toán đơn hàng</p>
                        </div>
                        <div class="content">
                            <h2>Cảm ơn bạn đã mua hàng, %s!</h2>
                            <p>Đơn hàng của bạn đã được xác nhận và đang được xử lý.</p>

                            <div class="order-info">
                                <h3>Thông tin đơn hàng</h3>
                                <p><strong>Mã đơn hàng:</strong> %s</p>
                                <p><strong>Tổng tiền:</strong> %s VNĐ</p>
                                <p><strong>Trạng thái:</strong> Đang xử lý</p>
                            </div>

                            <div class="order-info">
                                <h3>Chi tiết sản phẩm</h3>
                                %s
                            </div>

                            <p>Chúng tôi sẽ gửi email cập nhật khi đơn hàng được vận chuyển.</p>
                        </div>
                        <div class="footer">
                            <p>© 2024 Trường LCD. Tất cả quyền được bảo lưu.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(customerName, orderCode, totalAmount, orderItems);
    }

    // Template cho email cập nhật trạng thái đơn hàng
    private String buildOrderStatusUpdateEmail(String customerName, String orderCode,
            String oldStatus, String newStatus, String notes) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background: #ffc107; color: #333; padding: 20px; text-align: center; }
                        .content { padding: 20px; background: #f9f9f9; }
                        .status-info { background: white; padding: 15px; border-radius: 5px; margin: 15px 0; }
                        .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Trường LCD</h1>
                            <p>Cập nhật đơn hàng</p>
                        </div>
                        <div class="content">
                            <h2>Xin chào %s!</h2>
                            <p>Đơn hàng của bạn đã được cập nhật trạng thái.</p>

                            <div class="status-info">
                                <h3>Thông tin cập nhật</h3>
                                <p><strong>Mã đơn hàng:</strong> %s</p>
                                <p><strong>Trạng thái cũ:</strong> %s</p>
                                <p><strong>Trạng thái mới:</strong> %s</p>
                                %s
                            </div>

                            <p>Cảm ơn bạn đã tin tưởng và sử dụng dịch vụ của chúng tôi!</p>
                        </div>
                        <div class="footer">
                            <p>© 2024 Trường LCD. Tất cả quyền được bảo lưu.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(customerName, orderCode, oldStatus, newStatus,
                notes != null ? "<p><strong>Ghi chú:</strong> " + notes + "</p>" : "");
    }

    // Template cho email thông báo bình luận mới
    private String buildNewCommentNotificationEmail(String productName, String commenterName,
            String comment, String productUrl) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background: #dc3545; color: white; padding: 20px; text-align: center; }
                        .content { padding: 20px; background: #f9f9f9; }
                        .comment-info { background: white; padding: 15px; border-radius: 5px; margin: 15px 0; }
                        .button { display: inline-block; background: #007bff; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                        .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Trường LCD - Admin</h1>
                            <p>Bình luận mới</p>
                        </div>
                        <div class="content">
                            <h2>Có bình luận mới!</h2>
                            <p>Một khách hàng đã bình luận về sản phẩm của bạn.</p>

                            <div class="comment-info">
                                <h3>Thông tin bình luận</h3>
                                <p><strong>Sản phẩm:</strong> %s</p>
                                <p><strong>Người bình luận:</strong> %s</p>
                                <p><strong>Nội dung:</strong></p>
                                <div style="background: #f8f9fa; padding: 10px; border-radius: 3px; margin: 10px 0;">
                                    %s
                                </div>
                            </div>

                            <a href="%s" class="button">Xem sản phẩm</a>
                        </div>
                        <div class="footer">
                            <p>© 2024 Trường LCD. Tất cả quyền được bảo lưu.</p>
                        </div>
                    </div>
                </body>
                </html>
                """
                .formatted(productName, commenterName, comment, productUrl);
    }

    // Template cho email OTP
    private String buildOtpEmail(String otpCode, String purpose) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background: #007bff; color: white; padding: 20px; text-align: center; }
                        .content { padding: 20px; background: #f9f9f9; }
                        .otp-code { background: #007bff; color: white; font-size: 32px; font-weight: bold; padding: 20px; text-align: center; border-radius: 10px; margin: 20px 0; letter-spacing: 5px; }
                        .warning { background: #fff3cd; border: 1px solid #ffeaa7; padding: 15px; border-radius: 5px; margin: 15px 0; }
                        .footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Trường LCD</h1>
                            <p>Mã xác thực OTP</p>
                        </div>
                        <div class="content">
                            <h2>Mã OTP của bạn</h2>
                            <p>Chúng tôi nhận được yêu cầu %s cho tài khoản của bạn.</p>
                            <p>Sử dụng mã OTP sau để xác thực:</p>
                            
                            <div class="otp-code">%s</div>
                            
                            <div class="warning">
                                <p><strong>⚠️ Lưu ý quan trọng:</strong></p>
                                <ul>
                                    <li>Mã OTP này chỉ có hiệu lực trong <strong>1 phút</strong></li>
                                    <li>Không chia sẻ mã này với bất kỳ ai</li>
                                    <li>Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email</li>
                                </ul>
                            </div>
                            
                            <p>Cảm ơn bạn đã tin tưởng và sử dụng dịch vụ của chúng tôi!</p>
                        </div>
                        <div class="footer">
                            <p>© 2024 Trường LCD. Tất cả quyền được bảo lưu.</p>
                        </div>
                    </div>
                </body>
                </html>
                """
                .formatted(purpose, otpCode);
    }
}
