package backend.main.Service.ServiceImp.Product;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import backend.main.Config.Engine;
import backend.main.Config.LoggerE;
import backend.main.DTO.ImageDTO;
import backend.main.DTO.Product.ImageProjection;
import backend.main.DTO.Product.ProductDTO;
import backend.main.DTO.ProductSpecificationDTO;
import backend.main.DTO.Product.VariantDTO;
import backend.main.Model.InventoryItem;
import backend.main.Model.ResponseObject;
import backend.main.Model.Product.ProductVariant;
import backend.main.Model.Product.Products;
import backend.main.Repository.ProductRepository;
import backend.main.Request.ProductRequest;
import backend.main.Service.BaseService;
import backend.main.Service.InventoryService;
import backend.main.Service.ProductImageService;
import backend.main.Service.VariantService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ProductService implements BaseService<Products, Integer> {

    private final ProductSpecificationService productSpecificationService;

    ProductService(ProductSpecificationService pSpecificationRepository) {
        this.productSpecificationService = pSpecificationRepository;
    }

    ResponseEntity<ResponseObject> addProduct(ProductRequest productRequest) {
        return null;
    }

    ResponseEntity<ResponseObject> update(ProductRequest productRequest) {
        return null;
    }

    @Autowired
    private ProductRepository repository;
    @Autowired
    private VariantService variantService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private InventoryService inventoryService;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductService.class);   

    public ResponseEntity<ResponseObject> findAll() {
        List<Products> products = repository.findAll();

        List<ProductDTO> dtos = products.stream().map(product -> {
            ProductDTO dto = convertProductDTO(product);
            // Map variants
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

            dto.setImages(listimg);
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
        dto.setImages(listimg);

        return new ResponseEntity<>(
                new ResponseObject(200, "Thành công", 0, dto),
                HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> findAllByCateId(Integer id) {
        List<Products> products = repository.findAllByCateIdOrChild(id);
        List<ProductDTO> dtos = products.stream().map(product -> {
            ProductDTO dto = convertProductDTO(product);
            // Map variants
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
            dto.setImages(listimg);
            return dto;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(new ResponseObject(200,
                "Thành công",
                0,
                dtos),
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

            List<ImageDTO> listimg = new ArrayList<>();
            List<ImageProjection> listimgs = repository.listNativeImgById(dto.getId());
            if (listimgs != null && !listimgs.isEmpty()) {
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

            dto.setImages(listimg);
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
        Products saved = repository.save(entity);
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

        // 🔒 Kiểm tra tránh NullPointerException
        if (saved != null && saved.getId() != null) {
            logger.info("Save Successfully : Id: " + saved.getId() + " Name: " + saved.getName());
            if (entity.getVariants() != null && !entity.getVariants().isEmpty()) {
                Map<String, ProductVariant> keyToClientVariant = new HashMap<>();
                for (ProductVariant v : entity.getVariants()) {
                    String key = (v.getSku() != null && !v.getSku().isEmpty()) ? ("SKU:" + v.getSku())
                            : ("CS:" + (v.getColor() == null ? "" : v.getColor()) + "|"
                                    + (v.getStorage() == null ? "" : v.getStorage()));
                    keyToClientVariant.put(key, v);
                }
                for (ProductVariant savedVar : saved.getVariants()) {
                    String key = (savedVar.getSku() != null && !savedVar.getSku().isEmpty())
                            ? ("SKU:" + savedVar.getSku())
                            : ("CS:" + (savedVar.getColor() == null ? "" : savedVar.getColor()) + "|"
                                    + (savedVar.getStorage() == null ? "" : savedVar.getStorage()));
                    ProductVariant clientVar = keyToClientVariant.get(key);
                    if (clientVar != null && clientVar.getInventoryItem() != null) {
                        InventoryItem invReq = clientVar.getInventoryItem();
                        InventoryItem inventoryItem = inventoryService.findItembyVariant(savedVar.getId())
                                .orElse(new InventoryItem());
                        inventoryItem.setProductVariant(savedVar);
                        inventoryItem.setStock(invReq.getStock());
                        inventoryItem.setStatus(invReq.getStatus());
                        inventoryItem.setWarrantyMonths(invReq.getWarrantyMonths());
                        savedVar.setInventoryItem(inventoryItem);
                        inventoryService.createNew(inventoryItem);
                    }
                }
            }

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
        logger.error("Save Failed: {}" , (saved != null ? saved.getName() : "null"));
        return new ResponseEntity<>(
                new ResponseObject(500, "Tạo mới thất bại", 1, null),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        Optional<Products> optional = repository.findById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy danh mục với ID: " + id, 1, null),
                    HttpStatus.NOT_FOUND);
        }
        try {
            productImageService.deleteImagesByProductId(id);
            optional.get().getVariants().forEach(variant -> {
                inventoryService.deleteByProductVariantId(variant.getId());
                variantService.delete(variant.getId());
            });
            repository.deleteById(id);
            logger.info("Delete Successfully ID: {}" , id);
            return new ResponseEntity<>(
                    new ResponseObject(200, "Xóa thành công", 0, null),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Delete Exception: {}" , e.getMessage());
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
            logger.info("Không tìm thấy sản phẩm với ID: {}" , entity.getId());
            return createNew(entity);
        }

        try {
            Products existingProduct = optional.get();

            // --- Cập nhật thông tin sản phẩm ---
            existingProduct.setName(entity.getName());
            existingProduct.setSlug(entity.getSlug());
            existingProduct.setDescription(entity.getDescription());
            existingProduct.setSpecifications(entity.getSpecifications());
            existingProduct.setBrand(entity.getBrand());
            existingProduct.setModel(entity.getModel());
            existingProduct.setCategory(entity.getCategory());
            existingProduct.setIsActive(entity.getIsActive());
            existingProduct.setIsFeatured(entity.getIsFeatured());
            existingProduct.setIsHot(entity.getIsHot());

            // --- Xử lý variants ---
            List<ProductVariant> updatedVariants = new ArrayList<>();
            for (ProductVariant variant : entity.getVariants()) {
                ProductVariant variantEntity;
                if (variant.getId() != null) {
                    // Variant cũ, lấy từ DB
                    ResponseEntity<ResponseObject> vResp = variantService.findVariantById(variant.getId());
                    ResponseObject vBody = vResp != null ? vResp.getBody() : null;
                    ProductVariant safeVariant = vBody != null ? (ProductVariant) vBody.getData() : null;
                    variantEntity = safeVariant != null ? safeVariant : new ProductVariant();
                } else {
                    // Variant mới
                    variantEntity = new ProductVariant();
                }
                // Gán thông tin variant
                variantEntity.setNameVariants(variant.getNameVariants());
                variantEntity.setCostPrice(variant.getCostPrice());
                variantEntity.setSalePrice(variant.getSalePrice());
                variantEntity.setListPrice(variant.getListPrice());
                variantEntity.setColor(variant.getColor());
                variantEntity.setColorCode(variant.getColorCode());
                variantEntity.setStorage(variant.getStorage());
                variantEntity.setRam(variant.getRam());
                variantEntity.setSku(variant.getSku() != null ? variant.getSku()
                        : Engine.makeSKU(Engine.generateShortName(entity.getName())));
                variantEntity.setIsActive(variant.getIsActive());
                variantEntity.setProduct(existingProduct);

                updatedVariants.add(variantEntity);
            }
            existingProduct.setVariants(updatedVariants);

            // --- Lưu product + variants trước để có ID ---
            Products savedProduct = repository.save(existingProduct);

            // --- Cập nhật / tạo InventoryItem ---
            Map<String, ProductVariant> keyToClientVariant = new HashMap<>();
            for (ProductVariant v : entity.getVariants()) {
                String key = (v.getId() != null) ? ("ID:" + v.getId())
                        : ((v.getSku() != null && !v.getSku().isEmpty()) ? ("SKU:" + v.getSku())
                                : ("CS:" + (v.getColor() == null ? "" : v.getColor()) + "|"
                                        + (v.getStorage() == null ? "" : v.getStorage())));
                keyToClientVariant.put(key, v);
            }
            for (ProductVariant variant : savedProduct.getVariants()) {
                String key = (variant.getId() != null) ? ("ID:" + variant.getId())
                        : ((variant.getSku() != null && !variant.getSku().isEmpty()) ? ("SKU:" + variant.getSku())
                                : ("CS:" + (variant.getColor() == null ? "" : variant.getColor()) + "|"
                                        + (variant.getStorage() == null ? "" : variant.getStorage())));
                ProductVariant clientVar = keyToClientVariant.get(key);
                InventoryItem invData = clientVar != null ? clientVar.getInventoryItem() : null;

                if (invData != null) {
                    InventoryItem inventoryItem = inventoryService.findItembyVariant(variant.getId())
                            .orElse(new InventoryItem());
                    inventoryItem.setProductVariant(variant);
                    inventoryItem.setStock(invData.getStock());
                    inventoryItem.setWarrantyMonths(invData.getWarrantyMonths());
                    inventoryService.createNew(inventoryItem);
                    variant.setInventoryItem(inventoryItem);
                }
            }
            Map<String, Object> map = new HashMap<>();
            ProductDTO temp = convertProductDTO(savedProduct);
            map.put("Object", temp);
            map.put("Listnew", temp.getSpecifications());
            return new ResponseEntity<>(
                    new ResponseObject(200, "Cập nhật thành côngddd123123123", 0, map),
                    HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Update Exception: {}" , e.getMessage());
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
                    logger.info("Delete Successfully ID: {}" , id);
                } else {
                    logger.warn("ID not found: {}" , id);
                }
            }
            return new ResponseEntity<>(
                    new ResponseObject(200, "Xóa thành công", 0, null),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Delete Exception: {}" , e.getMessage());
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
        if (product.getSpecifications() != null && !product.getSpecifications().isBlank()) {
            List<ProductSpecificationDTO> listnew = new Gson().fromJson(product.getSpecifications(),
                    new TypeToken<List<ProductSpecificationDTO>>() {
                    }.getType());
            dto.setSpecifications(listnew);
        }

        if (dto.getSpecifications() == null) {
            dto.setSpecifications(productSpecificationService.findAllByProductId(product.getId()));
        }
        dto.setIsActive(product.getIsActive());
        dto.setIsFeatured(product.getIsFeatured());
        dto.setIsHot(product.getIsHot());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        List<VariantDTO> variantDTOs = new ArrayList<>();
        if (product.getVariants() != null) {
            variantDTOs = variantService.convertVariantDTO(product.getVariants());
        }
        dto.setVariants(variantDTOs);
        return dto;
    }
    public List<ProductSpecificationDTO> getSpecsByProduct(int productId) {
        List<Object[]> results = repository.getProductSpecifications(productId);
        List<ProductSpecificationDTO> list = new ArrayList<>();
        for (Object[] row : results) {
            ProductSpecificationDTO dto = new ProductSpecificationDTO();
            // dto.setId((Integer) row[0]);
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
