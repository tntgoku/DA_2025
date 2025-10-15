package backend.main.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import backend.main.Model.ResponseObject;
import backend.main.Model.Promotion.Discount;
import backend.main.Repository.DiscountRepository;

@Service
public class PromotionService implements BaseService<Discount, Integer> {
    @Autowired
    private DiscountRepository discountRepository;

    public ResponseEntity<ResponseObject> findAll() {
        return new ResponseEntity<>(new ResponseObject(200,
                null, 0, discountRepository.findAll()), HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> findDiscountForProduct() {
        return new ResponseEntity<>(new ResponseObject(200,
                null, 0, discountRepository.getDiscountInventoryNative()), HttpStatus.OK);
    }

    public ResponseEntity<ResponseObject> findAllPromotion() {
        return new ResponseEntity<>(new ResponseObject(200,
                null, 0, discountRepository.findAll()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseObject> createNew(Discount entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> delete(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> update(Discount entity) {
        // TODO Auto-generated method stub
        return null;
    }

}
