package backend.main.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.Model.ResponseObject;
import backend.main.Repository.SpecicifcationRepository;

@RestController
@RequestMapping("/api/specification")
public class ProductSpecificationController {
    @Autowired
    private SpecicifcationRepository repository;

    @GetMapping
    public ResponseEntity<ResponseObject> getproductall() {
        return new ResponseEntity<>(new ResponseObject(200, "OKe", 0, repository.findAll()), HttpStatus.OK);
    }

    @GetMapping({ "/category/{categoryId}" })
    public ResponseEntity<ResponseObject> getProductSpecByidCate(@PathVariable Integer categoryId) {
        return new ResponseEntity<>(new ResponseObject(200, "OKe", 0, repository.findByCategoryId(categoryId)),
                HttpStatus.OK);
    }
}
