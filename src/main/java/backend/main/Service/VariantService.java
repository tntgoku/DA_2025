package backend.main.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import backend.main.Model.Categories;
import backend.main.Model.ResponseObject;
import backend.main.Model.Product.ProductVariant;
import backend.main.Model.Product.Products;
import backend.main.Repository.DiscountRepository;
import backend.main.Repository.InventoryReponsitory;
import backend.main.Repository.VariantReponsitory;
import backend.main.Request.VariantRequest;
import backend.main.Config.Engine;
import backend.main.Config.LoggerE;
import backend.main.DTO.*;
import backend.main.DTO.Product.VariantDTO;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;;

@Service
public class VariantService implements BaseService<ProductVariant, Integer> {

    private final CategoryService categoryService;

    @Autowired
    public VariantReponsitory reponsitory;
    @Autowired
    private DiscountRepository discountRepository;
    private final Logger logger = LoggerE.logger;
    @Autowired
    private InventoryReponsitory inventoryReponsitory;

    VariantService(CategoryService categoryService, CategoryService cate) {
        this.categoryService = cate;
    }

    public ResponseEntity<ResponseObject> findAllVariant() {
        List<ProductVariant> variants = reponsitory.findAll();

        if (variants.isEmpty()) {
            return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy dữ liệu", 0, null),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                new ResponseObject(200, "Lấy Data thành công", variants.size(), variants),
                HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> findVariantById(Integer id) {
        Optional<ProductVariant> variants = reponsitory.findById(id);
        if (variants.isPresent()) {
            return new ResponseEntity<>(
                    new ResponseObject(200, "Lấy Data thành công", 0, convertObject(variants.get())),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(
                new ResponseObject(404, "Không tìm thấy Product với ID: " + id, 1, null),
                HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<ResponseObject> findVariantByProductId(Integer id) {
        List<ProductVariant> listVariants = reponsitory.findByProduct(id);
        if (listVariants != null && !listVariants.isEmpty()) {
            List<VariantDTO> var = new ArrayList<>();
            listVariants.forEach(item -> {
                var.add(convertObject(item));
            });
            return new ResponseEntity<>(
                    new ResponseObject(200, "Lấy Data thành công", 0, var),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(
                new ResponseObject(404, "Không tìm thấy Product với ID: " + id, 1, null),
                HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ResponseObject> findAllInventory() {
        List<InventoryProjection> listitem = reponsitory.getItemInventory();
        if (listitem == null || listitem.isEmpty()) {
            return new ResponseEntity<>(
                    new ResponseObject(204,
                            "Không tìm thấy dữ liệu",
                            0,
                            null),
                    HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(
                new ResponseObject(200, "Lấy Data thành công", 0, listitem),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseObject> createNew(ProductVariant entity) {
        ProductVariant saved = reponsitory.save(entity);
        if (saved != null && saved.getId() != null) {

            logger.info("Save Successfully : Id: " + saved.getId() + "Name: " + saved.getNameVariants());
            return new ResponseEntity<>(
                    new ResponseObject(201, "Tạo mới thành công", 0, saved),
                    HttpStatus.CREATED);
        } else {
            logger.info("Save Failed  : " + "Name: " + saved.getNameVariants());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Tạo mới thất bại", 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> update(ProductVariant entity) {
        if (entity.getId() == null) {
            return new ResponseEntity<>(
                    new ResponseObject(400, "ID không được để trống", 1, null),
                    HttpStatus.BAD_REQUEST);
        }
        Optional<ProductVariant> optional = reponsitory.findById(entity.getId());
        if (!optional.isPresent()) {
            return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy danh mục với ID: " + entity.getId(), 1, null),
                    HttpStatus.NOT_FOUND);
        }
        try {
            logger.info("Update Successfully: ");
            reponsitory.save(entity);
            return new ResponseEntity<>(
                    new ResponseObject(200, "Cập nhật thành công", 0, entity),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.warning("Update Exception: " + e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Cập nhật thất bại: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ProductVariant convertVariantRequest(VariantRequest request, Products p) {
        ProductVariant a = new ProductVariant();
        logger.info("IdvariantId: " + request.getVariantId());
        a.setId(request.getVariantId());
        String namevariants = "";
        // Lấy đối tượng Category
        Categories category = p.getCategory();

        // Lấy Parent ID, xử lý an toàn vì nó là Integer (Wrapper) có thể NULL
        Integer parentId = category.getParent(); // Lưu ý: parent là kiểu Integer

        boolean isAccessory = category.getId() == 3;
        boolean isService = category.getId() == 5;
        boolean isTopLevel = parentId == null; // Kiểm tra Parent ID là null

        if ((isAccessory && isTopLevel) || (isService && isTopLevel)) {
            namevariants = p.getName();
        } else {
            namevariants = (p.getName() == null ? "" : p.getName()) + " " +
                    (request.getStorage() == null ? "" : request.getStorage()) + " " +
                    (request.getRegionCode() == null ? "" : request.getRegionCode());

        }
        a.setNameVariants(namevariants);
        if (request.getSku() == null || request.getSku().isEmpty()) {
            a.setSku(Engine.makeSKU(Engine.generateShortName(p.getName())));
        }
        a.setRam(request.getRam());
        a.setStorage(request.getStorage());
        a.setIsActive(request.getIsActive());
        a.setProduct(p);
        a.setColor(request.getColor());
        return a;
    }

    public List<VariantDTO> convertVariantDTO(List<ProductVariant> vProductVariants) {
        if (vProductVariants == null) {
            return Collections.emptyList();
        }
        return vProductVariants.stream()
                .map(this::convertObject)
                .collect(Collectors.toList());
    }

    public VariantDTO convertObject(ProductVariant v) {
        VariantDTO vd = new VariantDTO();
        vd.setVariantId(v.getId());
        vd.setNameVariants(v.getNameVariants());
        vd.setSku(v.getSku());
        vd.setColor(v.getColor());
        vd.setColorCode(v.getColorCode());
        vd.setStorage(v.getStorage());
        vd.setRam(v.getRam());
        vd.setRegionCode(v.getRegionCode());
        vd.setIsActive(v.getIsActive());
        vd.setCreatedAt(v.getCreatedAt());
        vd.setUpdatedAt(v.getUpdatedAt());
        vd.setProductId(v.getProduct().getId());
        discountRepository.getDiscountInventoryNative().forEach(item -> {
            if (item.getProductId() == v.getProduct().getId()) {
                vd.setDiscount(BigDecimal.valueOf(item.getValue()));
            }
        });
        inventoryReponsitory.findByProductVariant(v.getId()).forEach(itemv -> {
            vd.setStock(itemv.getStock());
            vd.setPrice(itemv.getCostPrice());
            vd.setList_price(itemv.getListPrice());
            vd.setSale_price(itemv.getSalePrice());
            vd.setWarrantly(itemv.getWarrantyMonths());
            vd.setStatus(itemv.getStatus());
        });

        return vd;
    }
}
