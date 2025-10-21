package backend.main.Request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class AdminResponseRequest {
    
    @NotBlank(message = "Phản hồi admin không được để trống")
    @Size(max = 1000, message = "Phản hồi admin không được vượt quá 1000 ký tự")
    private String response;
    
    // Validation methods
    public String getResponsePreview(int maxLength) {
        if (response == null) {
            return "";
        }
        if (response.length() <= maxLength) {
            return response;
        }
        return response.substring(0, maxLength) + "...";
    }
}
