package backend.main.Config;

// Đặt file này trong thư mục /exception hoặc /utils của project
public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException(String message) {
        super(message);
    }
}