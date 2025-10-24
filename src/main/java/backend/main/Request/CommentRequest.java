package backend.main.Request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class CommentRequest {
    
    @NotEmpty(message = "Product ID không được để trống")
    private Integer productId;
    
    @NotEmpty(message = "User ID không được để trống")
    private Integer userId;
    
    @NotEmpty(message = "Nội dung bình luận không được để trống")
    private String content;
    
    @NotEmpty(message = "Đánh giá phải từ 1 đến 5 sao")
    private Integer rating;
    
    private Boolean isAnonymous = false;
    
    private Integer parentCommentId; 
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
