package backend.main.Request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class CommentRequest {
    
    @NotNull(message = "Product ID không được để trống")
    private Integer productId;
    
    @NotNull(message = "User ID không được để trống")
    private Integer userId;
    
    @NotBlank(message = "Nội dung bình luận không được để trống")
    private String content;
    
    @Min(value = 1, message = "Đánh giá phải từ 1 đến 5 sao")
    @Max(value = 5, message = "Đánh giá phải từ 1 đến 5 sao")
    private Integer rating;
    
    private Boolean isAnonymous = false;
    
    private Integer parentCommentId; // For replies
    
    // Validation methods
    public boolean hasRating() {
        return rating != null && rating >= 1 && rating <= 5;
    }
    
    public boolean isReply() {
        return parentCommentId != null;
    }
    
    public String getContentPreview(int maxLength) {
        if (content == null) {
            return "";
        }
        if (content.length() <= maxLength) {
            return content;
        }
        return content.substring(0, maxLength) + "...";
    }
}
