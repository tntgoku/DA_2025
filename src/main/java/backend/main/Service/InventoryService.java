package backend.main.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import backend.main.Config.LoggerE;
import backend.main.Model.InventoryItem;
import backend.main.Model.ResponseObject;
import backend.main.Repository.InventoryReponsitory;
import backend.main.Request.InventoryRequest;

@Service
public class InventoryService implements BaseService<InventoryItem, Integer> {
    @Autowired
    private InventoryReponsitory reponsitory;
    private Logger logger = LoggerE.logger;

    public ResponseEntity<ResponseObject> findAll() {
        List<InventoryItem> listitem = reponsitory.findAll();
        if (listitem == null || listitem.isEmpty()) {
            return new ResponseEntity<>(new ResponseObject(204,
                    "Không tìm thấy", 0, null),
                    HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new ResponseObject(200,
                "Thành công", 0, reponsitory.findAll()),
                HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> findItembyId(Integer id) {
        Optional<InventoryItem> optional = reponsitory.findById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(
                    new ResponseObject(404,
                            "Không thành công",
                            0,
                            "Not found source with ID: " + id),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                new ResponseObject(200,
                        "Thành công",
                        0,
                        optional.get()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseObject> createNew(InventoryItem entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        InventoryItem saved = reponsitory.save(entity);
        if (saved != null && saved.getId() != null) {

            logger.info("Save Successfully : Id: " + saved.getId() + "Name: " + saved.getProductVariant());
            return new ResponseEntity<>(
                    new ResponseObject(201, "Tạo mới thành công", 0, saved),
                    HttpStatus.CREATED);
        } else {
            logger.info("Lưu vào kho thất bại : " + "Name: " + saved.getProductVariant());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Tạo mới thất bại", 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        Optional<InventoryItem> optional = reponsitory.findById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy Item với ID: " + id, 1, null),
                    HttpStatus.NOT_FOUND);
        }

        try {
            reponsitory.deleteById(id);
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
    public ResponseEntity<ResponseObject> update(InventoryItem entity) {
        Optional<InventoryItem> optional = reponsitory.findById(entity.getId());
        if (!optional.isPresent()) {
            return new ResponseEntity<>(
                    new ResponseObject(404, "Không tìm thấy Item với ID: " + entity.getId(), 1, null),
                    HttpStatus.NOT_FOUND);
        }

        try {
            InventoryItem update = reponsitory.save(entity);
            logger.info("Update Successfully ID: " + update.getId() + "\t" + update.getCostPrice());
            return new ResponseEntity<>(
                    new ResponseObject(200, "Xóa thành công", 0, null),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.warning("Update Exception: " + e.getMessage());
            return new ResponseEntity<>(
                    new ResponseObject(500, "Update thất bại: " + e.getMessage(), 1, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public InventoryItem convertRequestInventory(InventoryRequest request) {
        InventoryItem item = new InventoryItem();
        item.setId(request.getId());
        item.setProductVariant(request.getProductVariant());
        item.setCondition(request.getCondition());
        item.setSource(request.getSource());
        item.setImei(request.getImei());
        item.setSerialNumber(request.getSerialNumber());
        item.setCostPrice(request.getCostPrice());
        item.setSalePrice(request.getSalePrice());
        item.setListPrice(request.getListPrice());
        item.setStatus(request.getStatus());
        item.setPosition(request.getPosition());
        item.setStock(request.getStock());
        item.setImportDate(request.getImportDate());
        item.setSoldDate(request.getSoldDate());
        item.setWarrantyMonths(request.getWarrantyMonths());
        item.setDeviceConditionNotes(request.getDeviceConditionNotes());
        item.setPreviousOwnerInfo(request.getPreviousOwnerInfo());
        item.setCreatedAt(request.getCreatedAt());
        item.setUpdatedAt(request.getUpdatedAt());
        return item;
    }
}
