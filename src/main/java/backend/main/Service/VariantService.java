package backend.main.Service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import backend.main.Model.InventoryItem;
import backend.main.Model.ResponseObject;
import backend.main.Model.Product.ProductVariant;
import backend.main.Model.Product.Products;
import backend.main.Repository.DiscountRepository;
import backend.main.Repository.InventoryReponsitory;
import backend.main.Repository.VariantReponsitory;
import backend.main.Request.VariantRequest;
import jakarta.transaction.Transactional;
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

    @Autowired
    public VariantReponsitory reponsitory;
    @Autowired
    private DiscountRepository discountRepository;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(VariantService.class);
    @Autowired
    private InventoryReponsitory inventoryReponsitory;

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
    //Lay theo Entity
    public ResponseEntity<ResponseObject> findVariantById(Integer id) {
        Optional<ProductVariant> variants = reponsitory.findById(id);
        if (variants.isPresent()) {
            logger.info(("Lay thanh  cong Object nay ID:{}"), id    );
            return new ResponseEntity<>(
                    new ResponseObject(200, "Lấy Data thành công", 0, variants.get()),
                    HttpStatus.OK);
        } else {
            logger.info("Lay khong thanh cong Object nay ID:{}", id);
            return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy Product với ID: " + id, 1, null),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<ResponseObject> findVariantByProductId(Integer id) {
        List<ProductVariant> listVariants = reponsitory.findByProduct(id);
        Map<String, String> colorKeyToIdMap = new HashMap<>();
        List<ProductVariant> listVariantnotidColor = new ArrayList<>();
        List<ProductVariant> listVariantidColor = new ArrayList<>();
        listVariants.forEach(item -> {
          if(item.getColorCode() == null || item.getColorCode().isEmpty()){
            listVariantnotidColor.add(item);
          }else{
            listVariantidColor.add(item);
          }
        });
        listVariantnotidColor.forEach(item -> {
            listVariantidColor.forEach(item2 -> {
                if(item.getColor().equals(item2.getColor())){
                    item.setColorCode(item2.getColorCode());
                    if(item.getColorCode().isEmpty() || item.getColorCode() == null){
                        item.setColorCode(item2.getColorCode());
                        reponsitory.save(item);
                    }
                }
            });
        });
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

            logger.info("Save Successfully : Id:{}, Name: {}", saved.getId(), saved.getNameVariants());
            return new ResponseEntity<>(
                    new ResponseObject(201, "Tạo mới thành công", 0, saved),
                    HttpStatus.CREATED);
        } else {
            logger.info("Save Failed  : Name: {}", saved.getNameVariants());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Tạo mới thất bại", 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional
    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        reponsitory.deleteById(id);
        return new ResponseEntity<>(
                new ResponseObject(200, "Lấy Data thành công", 0, 1),
                HttpStatus.OK);
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
            logger.warn("Update Exception: {}" , e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Cập nhật thất bại: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Trong VariantService.java

    public ProductVariant convertVariantRequest(VariantRequest request, Products p) {
        ProductVariant a;
        // ----------------------------------------------------
        // PHẦN 1 & 2: XÁC ĐỊNH & CẬP NHẬT ProductVariant (Giữ nguyên)
        // ----------------------------------------------------
        logger.info("IdvariantId: {}" , request.getVariantId());

        if (request.getVariantId() != null && request.getVariantId() > 0) {
            // UPDATE: Tìm ProductVariant hiện tại
            a = reponsitory.findById(request.getVariantId())
                    .orElseThrow(() -> new RuntimeException("ProductVariant not found"));
        } else {
            // CREATE: Tạo mới ProductVariant
            a = new ProductVariant();
        }
        String namevariants = "";
        namevariants = (p.getName() == null ? "" : p.getName()) + " " +
                (request.getStorage() == null ? "" : request.getStorage()) + " " +
                (request.getRegionCode() == null ? "" : request.getRegionCode());
        a.setNameVariants(namevariants);

        if (request.getSku() == null || request.getSku().isEmpty()) {
            a.setSku(Engine.makeSKU(Engine.generateShortName(p.getName())));
        }
        a.setRam(request.getRam());
        a.setStorage(request.getStorage());
        a.setIsActive(request.getIsActive());
        a.setProduct(p);
        a.setColor(request.getColor());
        a.setColorCode(request.getColorCode());
        logger.info("No o day ne ::{}" , a.getColorCode());
        // Attach inventory data from request regardless of existing/new variant.
        if (request.getStock() != null || request.getPrice() != null || request.getList_price() != null
                || request.getSale_price() != null ||
                request.getStatus() != null || request.getWarrantly() != null) {
            InventoryItem inventoryItem;
            if (a.getId() != null) {
                inventoryItem = inventoryReponsitory.findByProductVariant_Id(a.getId()).orElse(new InventoryItem());
            } else {
                inventoryItem = new InventoryItem();
            }
            inventoryItem.setProductVariant(a);
            inventoryItem.setStock(request.getStock());
            a.setCostPrice(request.getPrice());
            a.setSalePrice(request.getSale_price());
            a.setListPrice(request.getList_price());
            inventoryItem.setStatus(request.getStatus());
            inventoryItem.setWarrantyMonths(request.getWarrantly());
            a.setInventoryItem(inventoryItem);
        }
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
        inventoryReponsitory.findByProductVariant_Id(v.getId())
                .ifPresent(itemv -> {
                    vd.setStock(itemv.getStock());
                    vd.setPrice(v.getCostPrice());
                    vd.setList_price(v.getListPrice());
                    vd.setSale_price(v.getSalePrice());
                    vd.setWarrantly(v.getInventoryItem().getWarrantyMonths());
                    vd.setStatus(v.getInventoryItem().getStatus());
                });

        return vd;
    }
}
