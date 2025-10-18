package backend.main.Service;

import backend.main.Config.ImageNotFoundException;
import backend.main.Model.Product.ProductImage;
import backend.main.Repository.ProductImageRepository;
import backend.main.Request.ProductImageRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductImageService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductImageService.class);
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private Path uploadPath;
    public List<ProductImage> saveProductImages(Integer productId, List<ProductImageRequest> imageRequests) {
        List<ProductImage> savedImages =new ArrayList<>();
        
        for (ProductImageRequest request : imageRequests) {
            ProductImage productImage = new ProductImage();
            productImage.setProductId(productId);
            productImage.setImageUrl(request.getImageUrl());
            productImage.setAltImg(request.getAltImg());
            productImage.setVariantId(request.getVariantId() != null ? request.getVariantId() : null);
            productImage.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
            productImage.setIsPrimary(request.getIsPrimary() != null ? request.getIsPrimary() : false);
            productImage.setImageType(request.getImageType() != null ? request.getImageType() : "gallery");
            ProductImage saved = productImageRepository.save(productImage);
            if(saved != null && saved.getId() != null) {
                savedImages.add(saved);
                logger.info("Save product image successfully: {}", request.toString());
            } else {
                logger.error("Save product image failed: {}", request.toString());
            }
        }
        return savedImages;
    }

    public List<ProductImage> getImagesByProductId(Integer productId) {
        return productImageRepository.findByProductId(productId);
    }

    public List<ProductImage> getImagesByVariantId(Integer variantId) {
        return productImageRepository.findByVariantId(variantId);
    }

    public Optional<ProductImage> getImageById(Integer id) {
        return productImageRepository.findById(id);
    }

    public void deleteImage(Integer id) {
        productImageRepository.deleteById(id);
    }

    @Transactional
    public void deleteImagesByProductId(Integer productId) {
        productImageRepository.deleteByProductId(productId);
    }

    @Transactional
    public void deleteImagesByVariantId(Integer variantId) {
        productImageRepository.deleteByVariantId(variantId);
    }

    @Transactional
    public void deleteImagesByProductIdAndVariantId(Integer productId, Integer variantId) {
        productImageRepository.deleteByProductIdAndVariantId(productId, variantId);
    }
    public byte[] getJpgImage(String imageId) throws ImageNotFoundException {
        List<ProductImage> images = productImageRepository.findAll();
        for (ProductImage image : images) {
            try {
            if (image.getProductId().equals(Integer.parseInt(imageId))) {
                String imageUrl = image.getImageUrl();
                logger.info("Image URL: {} " , imageUrl);
                String filename = imageUrl.replace("/uploads/", ""); 
                Path fullPath = this.uploadPath.resolve(filename).normalize();
                String extension = imageUrl.substring(imageUrl.lastIndexOf(".") + 1).toLowerCase();
                if (extension.equals("jpg") || extension.equals("jpeg")) {
                        return Files.readAllBytes(fullPath);
                }
                if (extension.equals("png")) {
                    return Files.readAllBytes(fullPath);
                }
                if (extension.equals("gif")) {
                    return Files.readAllBytes(fullPath);
                }
                if (extension.equals("webp")) {
                    return Files.readAllBytes(fullPath);
                }
                if (extension.equals("bmp")) {
                    return Files.readAllBytes(fullPath);
                }
                logger.error("Image not found with ID: {} " , image.getImageUrl());
                throw new ImageNotFoundException("Image not found with ID: " + imageId);
            }
            } catch (IOException e) {
                logger.error("Error reading image: {} " , e.getMessage());
                throw new ImageNotFoundException("Error reading image: " + e.getMessage());
            }
        }
        throw new ImageNotFoundException("Image not found with ID: " + imageId);
    }
    public byte[] getImageByIdImage(Integer imageId) throws ImageNotFoundException {
        Optional<ProductImage> image = productImageRepository.findById(imageId);
        if (image.isPresent()) {
            String imageUrl = image.get().getImageUrl();
            String filename = imageUrl.replace("/uploads/", ""); 
            Path fullPath = this.uploadPath.resolve(filename).normalize();
            try {
                return Files.readAllBytes(fullPath);
            } catch (IOException e) {
                logger.error("Error reading image: {} " , e.getMessage());
                throw new ImageNotFoundException("Error reading image: " + e.getMessage());
            }
        }
        throw new ImageNotFoundException("Image not found with ID: " + imageId);
}

}
