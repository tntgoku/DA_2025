package backend.main.Controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.Model.ResponseObject;
import backend.main.Model.Promotion.Voucher;
import backend.main.Repository.VoucherRepository;
import backend.main.Request.VoucherRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/voucher")
public class VoucherController {
    @Autowired
    private VoucherRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(VoucherController.class);
    @GetMapping()
    public  ResponseEntity<ResponseObject> getMethodName() {
        return new ResponseEntity<>(new ResponseObject(200, "Lấy voucher thành công", 1, repository.findAll()), HttpStatus.OK);
    }
    
    @GetMapping("/get-voucher/{voucherCode}")
    public ResponseEntity<ResponseObject> getVoucher(@PathVariable String voucherCode) {
        Voucher voucher = repository.findByCode(voucherCode.toUpperCase()).get(0);
        return new ResponseEntity<>(new ResponseObject(200, "Lấy voucher thành công", 1, voucher), HttpStatus.OK);
    }

    @PostMapping("/create-voucher")
    public ResponseEntity<ResponseObject> createVoucher(@RequestBody String voucherCode) {
        Voucher voucher = repository.findByCode(voucherCode.toUpperCase()).get(0);
        if (voucher == null) {
            return new ResponseEntity<>(new ResponseObject(400, "Voucher không tồn tại", 0, null), HttpStatus.BAD_REQUEST);
        }
        if (voucher.getIsActive() == false) {
            return new ResponseEntity<>(new ResponseObject(400, "Voucher không hoạt động", 0, null), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ResponseObject(200, "Tạo voucher thành công", 1, repository.save(voucher)), HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateVoucher(@PathVariable Integer id, @RequestBody VoucherRequest voucherRequest) {
        logger.info("VoucherRequest: {}", voucherRequest.toString());
       
        Voucher existingVoucher = repository.findById(id).get();
        if (existingVoucher == null) {
            return new ResponseEntity<>(new ResponseObject(400, "Voucher không tồn tại", 0, null), HttpStatus.BAD_REQUEST);
        }
        existingVoucher.setIsActive(voucherRequest.getIsActive());
        existingVoucher.setEndDate(LocalDateTime.parse(voucherRequest.getEndDate()));
        existingVoucher.setStartDate(LocalDateTime.parse(voucherRequest.getStartDate()));
        existingVoucher.setMaxUses(voucherRequest.getMaxUses());
        existingVoucher.setMaxUsesPerUser(voucherRequest.getMaxUsesPerUser());
        existingVoucher.setMinOrderValue(voucherRequest.getMinOrderValue());
        existingVoucher.setValue(voucherRequest.getValue());
        existingVoucher.setUpdatedAt(LocalDateTime.now());
        existingVoucher.setPriority(voucherRequest.getPriority());
        existingVoucher.setCode(voucherRequest.getCode().toUpperCase());
        existingVoucher.setName(voucherRequest.getName());
        existingVoucher.setDescription(voucherRequest.getDescription());
        existingVoucher.setDiscountType(voucherRequest.getDiscountType());
        existingVoucher.setMaxDiscount(voucherRequest.getMaxDiscount());
        existingVoucher.setTotalUses(voucherRequest.getTotalUses());
        existingVoucher.setMaxUsesPerUser(voucherRequest.getMaxUsesPerUser());
        Voucher savedVoucher = repository.save(existingVoucher);
        if (savedVoucher == null) {
            return new ResponseEntity<>(new ResponseObject(400, "Cập nhật voucher thất bại", 0, null), HttpStatus.BAD_REQUEST);
        }
        logger.info("Voucher updated: {}, {},   {},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}", savedVoucher.getId(),savedVoucher.getCode(),savedVoucher.getName(),savedVoucher.getDescription(),savedVoucher.getDiscountType(),savedVoucher.getMaxDiscount(),savedVoucher.getTotalUses(),savedVoucher.getMaxUses(),savedVoucher.getMaxUsesPerUser(),savedVoucher.getMinOrderValue(),savedVoucher.getValue(),savedVoucher.getStartDate(),savedVoucher.getEndDate(),savedVoucher.getIsActive(),savedVoucher.getPriority(),savedVoucher.getCreatedAt(),savedVoucher.getUpdatedAt());
        return new ResponseEntity<>(new ResponseObject(200, "Cập nhật voucher thành công", 1, savedVoucher), HttpStatus.OK);
    }
}
