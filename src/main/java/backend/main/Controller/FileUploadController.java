package backend.main.Controller;

import backend.main.Config.ImageNotFoundException;
import backend.main.Model.ResponseObject;
import backend.main.Model.Product.ProductImage;
import backend.main.Request.ProductImageRequest;
import backend.main.Service.ProductImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Autowired
    private ProductImageService productImageService;
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private Path uploadPath;

    @GetMapping(value = "/{imageId}")
    public ResponseEntity<byte[]> getImageById(@PathVariable String imageId) {

        try {
            byte[] imageData = productImageService.getJpgImage(imageId);
            org.springframework.http.MediaType mediaType = determineMediaType(imageId);
            return ResponseEntity
                    .ok()
                    .contentType(mediaType)
                    .body(imageData);

        } catch (ImageNotFoundException e) {
            // Xử lý khi không tìm thấy ảnh
            logger.error("Không tìm thấy ảnh với ID: {} - {}", imageId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Xử lý lỗi khác (ví dụ: lỗi IO)
            logger.error("Lỗi khi lấy ảnh với ID {}: {}", imageId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(value = "/{imageId}/{variantId}")
    public ResponseEntity<byte[]> getImageByIdAndVariantId(@PathVariable String imageId,
    @PathVariable String variantId) {

        try {
            byte[] imageData = productImageService.getImageByIdAndVariantId(imageId,variantId);
            org.springframework.http.MediaType mediaType = getImagesByProductIdAndVariantId(imageId,variantId);
            return ResponseEntity
                    .ok()
                    .contentType(mediaType)
                    .body(imageData);

        } catch (ImageNotFoundException e) {
            // Xử lý khi không tìm thấy ảnh
            logger.error("Không tìm thấy ảnh với ID: {} - {} - {}", imageId,variantId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Xử lý lỗi khác (ví dụ: lỗi IO)
            logger.error("Lỗi khi lấy ảnh với ID {}: {}", imageId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "imgSrc/{imageId}")
    public ResponseEntity<byte[]> getImageByIdImage(@PathVariable String imageId) {

        try {
            byte[] imageData = productImageService.getImageByIdImage(Integer.parseInt(imageId));
            org.springframework.http.MediaType mediaType = determineMediaType(imageId);
            return ResponseEntity
                    .ok()
                    .contentType(mediaType)
                    .body(imageData);

        } catch (ImageNotFoundException e) {
            // Xử lý khi không tìm thấy ảnh
            logger.error("Không tìm thấy ảnh với ID: {} - {}", imageId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Xử lý lỗi 
            logger.error("Lỗi khi lấy ảnh với ID {}: {}", imageId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    private org.springframework.http.MediaType determineMediaType(String imageId) {
        try {
            var imageOpt = productImageService.getImageById(Integer.parseInt(imageId));
            if (imageOpt.isPresent()) {
                String imageUrl = imageOpt.get().getImageUrl();
                String extension = imageUrl.substring(imageUrl.lastIndexOf(".") + 1).toLowerCase();

                return switch (extension) {
                    case "jpg", "jpeg" -> org.springframework.http.MediaType.IMAGE_JPEG;
                    case "png" -> org.springframework.http.MediaType.IMAGE_PNG;
                    case "gif" -> org.springframework.http.MediaType.IMAGE_GIF;
                    case "webp" -> org.springframework.http.MediaType.parseMediaType("image/webp");
                    case "bmp" -> org.springframework.http.MediaType.parseMediaType("image/bmp");
                    default -> org.springframework.http.MediaType.IMAGE_JPEG; 
                };
            }
        } catch (Exception e) {
            logger.warn("Không thể xác định MediaType cho ảnh ID {}: {}", imageId, e.getMessage());
        }

        return org.springframework.http.MediaType.IMAGE_JPEG;
    }

    private org.springframework.http.MediaType getImagesByProductIdAndVariantId(String imageId,String variantId) {
        try {
            List<ProductImage> images = productImageService.getImagesByProductIdAndVariantId(Integer.parseInt(imageId), Integer.parseInt(variantId));
            if (images != null && !images.isEmpty()) {
                String imageUrl = images.get(0).getImageUrl();
                String extension = imageUrl.substring(imageUrl.lastIndexOf(".") + 1).toLowerCase();

                return switch (extension) {
                    case "jpg", "jpeg" -> org.springframework.http.MediaType.IMAGE_JPEG;
                    case "png" -> org.springframework.http.MediaType.IMAGE_PNG;
                    case "gif" -> org.springframework.http.MediaType.IMAGE_GIF;
                    case "webp" -> org.springframework.http.MediaType.parseMediaType("image/webp");
                    case "bmp" -> org.springframework.http.MediaType.parseMediaType("image/bmp");
                    default -> org.springframework.http.MediaType.IMAGE_JPEG; 
                };
            }
        } catch (Exception e) {
            logger.warn("Không thể xác định MediaType cho ảnh ID {}: {}", imageId, e.getMessage());
        }

        return org.springframework.http.MediaType.IMAGE_JPEG;
    }


    @PostMapping("/image")
    public ResponseEntity<ResponseObject> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "variantId", required = false) String variantId,
            @RequestParam(value = "productId", required = false) String productId) {
        try {
            if (file.isEmpty()) {
                return new ResponseEntity<>(
                        new ResponseObject(400, "File is empty", 0, null),
                        HttpStatus.BAD_REQUEST);
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                return new ResponseEntity<>(
                        new ResponseObject(400, "File name is invalid", 0, null),
                        HttpStatus.BAD_REQUEST);
            }
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            String filename;
            if (variantId != null && !variantId.isEmpty()) {
                filename = "variant_" + variantId + "_" + System.currentTimeMillis() + fileExtension;
            } else if (productId != null && !productId.isEmpty()) {
                filename = "product_" + productId + "_" + System.currentTimeMillis() + fileExtension;
            } else {
                filename = UUID.randomUUID().toString() + fileExtension;
            }

            Path targetLocation = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = "/uploads/" + filename;

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("imageUrl", imageUrl);
            responseData.put("filename", filename);
            responseData.put("variantId", variantId);
            responseData.put("productId", productId);

            return new ResponseEntity<>(
                    new ResponseObject(200, "Upload successful", 1, responseData),
                    HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(
                    new ResponseObject(500, "Upload failed: " + e.getMessage(), 0, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/images")
    public ResponseEntity<ResponseObject> uploadMultipleImages(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "variantId", required = false) String variantId,
            @RequestParam(value = "productId", required = false) String productId) {
        try {
            List<Map<String, Object>> uploadedImages = new ArrayList<>();

            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                if (!file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();
                    if (originalFilename == null || originalFilename.isEmpty()) {
                        continue;
                    }
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

                    String filename;
                    if (variantId != null && !variantId.isEmpty()) {
                        // Đặt tên theo variant ID: variant_{variantId}_{index}_{timestamp}.ext
                        filename = "variant_" + variantId + "_" + i + "_" + System.currentTimeMillis() + fileExtension;
                    } else if (productId != null && !productId.isEmpty()) {
                        // Đặt tên theo product ID: product_{productId}_{index}_{timestamp}.ext
                        filename = "product_" + productId + "_" + i + "_" + System.currentTimeMillis() + fileExtension;
                    } else {
                        // Tên file mặc định với UUID
                        filename = UUID.randomUUID().toString().substring(0, 31) + fileExtension;
                    }

                    Path targetLocation = uploadPath.resolve(filename);
                    Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                    String imageUrl = "/uploads/" + filename;

                    Map<String, Object> imageData = new HashMap<>();
                    imageData.put("imageUrl", imageUrl);
                    imageData.put("filename", filename);
                    imageData.put("variantId", variantId);
                    imageData.put("productId", productId);
                    imageData.put("index", i);

                    uploadedImages.add(imageData);
                }
            }

            return new ResponseEntity<>(
                    new ResponseObject(200, "Upload successful", uploadedImages.size(), uploadedImages),
                    HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(
                    new ResponseObject(500, "Upload failed: " + e.getMessage(), 0, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/product/{productId}/images")
    public ResponseEntity<ResponseObject> saveProductImages(
            @PathVariable Integer productId,
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("metadata") String metadataJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<ProductImageRequest> metadata = mapper.readValue(metadataJson,
                    new TypeReference<List<ProductImageRequest>>() {
                    });

            logger.info("📦 Save product images for product {} -> {}", productId, metadataJson);

            List<Map<String, Object>> uploadedImages = new ArrayList<>();
            List<ProductImageRequest> productImages = new ArrayList<>();
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                ProductImageRequest meta = metadata.size() > i ? metadata.get(i) : new ProductImageRequest();
                Map<String, Object> imageData = new HashMap<>();
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || originalFilename.isEmpty()) {
                    continue; // Skip invalid files
                }
                String filename = "product_" + productId + "_" + System.currentTimeMillis() + "_" + originalFilename;
                Path targetLocation = uploadPath.resolve(filename);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                String imageUrl = "/uploads/" + filename;
                imageData.put("productId", productId);
                imageData.put("variantId", meta.getVariantId());
                imageData.put("altImg", meta.getAltImg());
                imageData.put("displayOrder", meta.getDisplayOrder());
                imageData.put("isPrimary", meta.getIsPrimary());
                imageData.put("imageType", meta.getImageType());
                imageData.put("imageUrl", imageUrl);
                productImages.add(new ProductImageRequest(
                        imageUrl,
                        meta.getAltImg(),
                        meta.getVariantId(),
                        meta.getProductId(),
                        meta.getDisplayOrder(),
                        meta.getIsPrimary(),
                        meta.getImageType()));
                uploadedImages.add(imageData);
            }
            productImageService.saveProductImages(productId, productImages);
            logger.info("✅ Uploaded {} images successfully", uploadedImages.size());
            return new ResponseEntity<>(
                    new ResponseObject(200, "Upload successful", uploadedImages.size(), uploadedImages), HttpStatus.OK);
        } catch (IOException e) {
            logger.error("❌ Upload failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject(500, "Upload failed: " + e.getMessage(), 0, null));
        }
    }
}