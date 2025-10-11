package backend.main.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import backend.main.Config.Engine;
import backend.main.Config.LoggerE;
import backend.main.DTO.ImageDTO;
import backend.main.DTO.Product.ImageProjection;
import backend.main.DTO.Product.ProductDTO;
import backend.main.DTO.Product.ProductSpecificationDTO;
import backend.main.DTO.Product.VariantDTO;
import backend.main.Model.ResponseObject;
import backend.main.Model.Product.ProductVariant;
import backend.main.Model.Product.Products;
import backend.main.Repository.InventoryReponsitory;
import backend.main.Repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ProductService implements BaseService<Products, Integer> {

    private final CategoryService categoryService;
    @Autowired
    private ProductRepository repository;
    @Autowired
    private VariantService variantService;
    private final Logger logger = LoggerE.logger;

    ProductService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public ResponseEntity<ResponseObject> findAll() {
        List<Products> products = repository.findAll();

        List<ProductDTO> dtos = products.stream().map(product -> {
            ProductDTO dto = convertProductDTO(product);
            // Map variants
            List<VariantDTO> variantDTOs = new ArrayList<>();
            List<ImageDTO> listimg = new ArrayList<>();
            repository.listNativeImg().forEach(img -> {
                if (img.getProductId() == product.getId()) {
                    ImageDTO item = new ImageDTO();
                    item.setId(img.getid());
                    item.setImgSrc(img.getImageUrl());
                    item.setImgAlt(img.getAltImg());
                    item.setDisplayOrder(img.getDisplayOrder());
                    item.setIsPrimary(img.getIsPrimary());
                    listimg.add(item);
                }
            });

            if (product.getVariants() != null) {
                variantDTOs = variantService.convertVariantDTO(product.getVariants());
            }
            dto.setImages(listimg);
            dto.setVariants(variantDTOs);
            return dto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(new ResponseObject(200,
                "Thành công",
                0,
                dtos),
                HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> findProductById(Integer id) {
        Optional<Products> optionalProduct = repository.findById(id);

        if (optionalProduct.isEmpty()) {
            return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy sản phẩm", 0, null),
                    HttpStatus.NOT_FOUND);
        }

        Products product = optionalProduct.get();
        ProductDTO dto = convertProductDTO(product);

        // Lấy danh sách ảnh
        List<ImageDTO> listimg = repository.listNativeImgById(product.getId())
                .stream()
                .map(img -> {
                    ImageDTO item = new ImageDTO();
                    item.setId(img.getid());
                    item.setImgSrc(img.getImageUrl());
                    item.setImgAlt(img.getAltImg());
                    item.setDisplayOrder(img.getDisplayOrder());
                    item.setIsPrimary(img.getIsPrimary());
                    return item;
                })
                .collect(Collectors.toList());

        // Lấy danh sách variants
        List<VariantDTO> variantDTOs = new ArrayList<>();
        if (product.getVariants() != null && !product.getVariants().isEmpty()) {
            variantDTOs = variantService.convertVariantDTO(product.getVariants());
        }

        dto.setImages(listimg);
        dto.setVariants(variantDTOs);

        return new ResponseEntity<>(
                new ResponseObject(200, "Thành công", 0, dto),
                HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> listimg() {
        return new ResponseEntity<>(new ResponseObject(200,
                "Thành công",
                0,
                repository.listNativeImg()),
                HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> findAllisFeature() {
        List<Products> products = repository.findByIsFeaturedTrue();

        List<ProductDTO> dtos = products.stream().map(product -> {
            ProductDTO dto = convertProductDTO(product);

            // Map variants
            List<VariantDTO> variantDTOs = new ArrayList<>();
            List<ImageDTO> listimg = new ArrayList<>();
            List<ImageProjection> listimgs = repository.listNativeImgById(dto.getId());
            if (listimgs != null || !listimg.isEmpty()) {

                listimgs.forEach(img -> {
                    ImageDTO item = new ImageDTO();
                    item.setId(img.getid());
                    item.setImgSrc(img.getImageUrl());
                    item.setImgAlt(img.getAltImg());
                    item.setDisplayOrder(img.getDisplayOrder());
                    item.setIsPrimary(img.getIsPrimary());
                    listimg.add(item);
                });
            }

            if (product.getVariants() != null) {
                variantDTOs = variantService.convertVariantDTO(product.getVariants());
            }

            dto.setImages(listimg);
            dto.setVariants(variantDTOs);
            return dto;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(new ResponseObject(200,
                "Thành công",
                0,
                dtos),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseObject> createNew(Products entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        // ===== Xử lý variants =====
        if (entity.getVariants() != null && !entity.getVariants().isEmpty()) {
            for (ProductVariant variant : entity.getVariants()) {
                variant.setProduct(entity); // gắn quan hệ ngược lại
                variant.setCreatedAt(LocalDateTime.now());
                variant.setUpdatedAt(LocalDateTime.now());
            }
            logger.info("Cai nay dc roi ne");
        } else {
            logger.info("Sản phẩm không có biến thể nào");
        }

        Products saved = repository.save(entity);

        // 🔒 Kiểm tra tránh NullPointerException
        if (saved != null && saved.getId() != null) {
            logger.info("Save Successfully : Id: " + saved.getId() + " Name: " + saved.getName());

            return new ResponseEntity<>(
                    new ResponseObject(
                            200,
                            "Tạo mới thành công Product " +
                                    ((entity.getVariants() != null && !entity.getVariants().isEmpty()) ? "Có biến thể"
                                            : "Không có biến thể"),
                            0,
                            saved),
                    HttpStatus.CREATED);
        }

        // Trường hợp lưu thất bại
        logger.warning("Save Failed: " + (saved != null ? saved.getName() : "null"));
        return new ResponseEntity<>(
                new ResponseObject(500, "Tạo mới thất bại", 1, null),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        Optional<Products> optional = repository.findById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy danh mục với ID: " + id, 1, null),
                    HttpStatus.NOT_FOUND);
        }

        try {
            repository.deleteById(id);
            logger.info("Delete Successfully ID: " + id);
            return new ResponseEntity<>(
                    new ResponseObject(200, "Xóa thành công", 0, null),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.warning("Delete Exception: " + e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Xóa thất bại: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject> update(Products entity) {
        if (entity.getId() == null) {
            return new ResponseEntity<>(
                    new ResponseObject(400, "ID không được để trống", 1, null),
                    HttpStatus.BAD_REQUEST);
        }
        Optional<Products> optional = repository.findById(entity.getId());
        if (!optional.isPresent()) {
            return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy danh mục với ID: " + entity.getId(), 1, null),
                    HttpStatus.NOT_FOUND);
        }
        try {
            for (ProductVariant variant : entity.getVariants()) {
                variant.setProduct(entity);
            }
            Products updated = repository.save(entity);
            logger.info("Update Successfully: " + entity.getId() + entity.getCategory().getId());
            return new ResponseEntity<>(
                    new ResponseObject(200, "Cập nhật thành công", 0, updated),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.warning("Update Exception: " + e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Cập nhật thất bại: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> deletes(List<Integer> list) {
        try {
            for (Integer id : list) {
                if (repository.existsById(id)) { // kiểm tra tồn tại trước khi xóa
                    repository.deleteById(id);
                    logger.info("Delete Successfully ID: " + id);
                } else {
                    logger.warning("ID not found: " + id);
                }
            }
            return new ResponseEntity<>(
                    new ResponseObject(200, "Xóa thành công", 0, null),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.warning("Delete Exception: " + e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Xóa thất bại: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ProductDTO convertProductDTO(Products product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setCategory(product.getCategory().getId());
        dto.setProductType(product.getProductType());
        dto.setName(product.getName());
        dto.setSlug(product.getSlug());
        dto.setDescription(product.getDescription());
        dto.setBrand(product.getBrand());
        dto.setModel(product.getModel());
        dto.setSpecifications(getSpecsByProduct(product.getId()));
        dto.setIsActive(product.getIsActive());
        dto.setIsFeatured(product.getIsFeatured());
        dto.setIsHot(product.getIsHot());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());

        return dto;
    }

    public List<ProductSpecificationDTO> getSpecsByProduct(int productId) {
        List<Object[]> results = repository.getProductSpecifications(productId);
        List<ProductSpecificationDTO> list = new ArrayList<>();

        for (Object[] row : results) {
            ProductSpecificationDTO dto = new ProductSpecificationDTO();
            dto.setId((Integer) row[0]);
            dto.setProductId((Integer) row[1]);
            dto.setSpecId((Integer) row[2]);
            dto.setValue((String) row[3]);
            dto.setLabel((String) row[4]);
            dto.setUnitName((String) row[5]);
            list.add(dto);
        }
        return list;
    }

}
