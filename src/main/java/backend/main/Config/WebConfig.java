package backend.main.Config; // Đặt trong gói Config của bạn

import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private Path uploadPath; // Inject Bean Path đã được định nghĩa

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Lấy đường dẫn tuyệt đối dưới dạng String, thêm "file:///" để tạo URL File System
        String absolutePath = uploadPath.toAbsolutePath().toString().replace("\\", "/");
        String fileUri = "file:///" + absolutePath + "/";
        
        // 1. Ánh xạ request URL "/uploads/**" 
        // 2. Tới thư mục vật lý được định nghĩa bởi Path uploadPath
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(fileUri);
    }
}