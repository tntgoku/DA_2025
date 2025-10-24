package backend.main.DTO.Comment;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductCommentDTO {
    private Integer id;
    private Integer productId;
    private String productName;
    private Integer userId;
    private String userName;
    private String userEmail;
    private String userAvatar;
    private String content;
    private Integer rating;
    private Boolean isApproved;
    private Boolean isAnonymous;
    private Integer parentCommentId;
    private Integer likesCount;
    private Integer dislikesCount;
    private Integer reportedCount;
    private String adminResponse;
    private LocalDateTime adminResponseAt;
    private String ipAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private java.util.List<ProductCommentDTO> replies;
    
    public boolean isReply() {
        return parentCommentId != null;
    }
    
    public String getDisplayName() {
        if (isAnonymous != null && isAnonymous) {
            return "Khách hàng ẩn danh";
        }
        return userName != null ? userName : "Người dùng";
    }
    
    public String getMaskedEmail() {
        if (userEmail == null || userEmail.isEmpty()) {
            return "";
        }
        String[] parts = userEmail.split("@");
        if (parts.length == 2) {
            String username = parts[0];
            if (username.length() <= 2) {
                return username + "@" + parts[1];
            }
            return username.substring(0, 2) + "***@" + parts[1];
        }
        return userEmail;
    }
    
    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "";
        }
        return createdAt.toString();
    }
    
    public String getFormattedUpdatedAt() {
        if (updatedAt == null) {
            return "";
        }
        return updatedAt.toString();
    }
    
    public String getFormattedAdminResponseAt() {
        if (adminResponseAt == null) {
            return "";
        }
        return adminResponseAt.toString();
    }
    
    public boolean hasAdminResponse() {
        return adminResponse != null && !adminResponse.trim().isEmpty();
    }
    
    public boolean hasReplies() {
        return replies != null && !replies.isEmpty();
    }
    
    public int getRepliesCount() {
        return replies != null ? replies.size() : 0;
    }
    
    public String getRatingStars() {
        if (rating == null || rating < 1 || rating > 5) {
            return "";
        }
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < rating; i++) {
            stars.append("★");
        }
        for (int i = rating; i < 5; i++) {
            stars.append("☆");
        }
        return stars.toString();
    }
    
    public String getShortContent(int maxLength) {
        if (content == null) {
            return "";
        }
        if (content.length() <= maxLength) {
            return content;
        }
        return content.substring(0, maxLength) + "...";
    }
}
