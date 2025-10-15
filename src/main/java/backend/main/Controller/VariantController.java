package backend.main.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.main.Config.Engine;
import backend.main.Model.ResponseObject;
import backend.main.Model.Product.ProductVariant;
import backend.main.Service.VariantService;

@RestController
@RequestMapping("/api/product/variant")
public class VariantController {
    @Autowired
    private VariantService variantService;

    @GetMapping()
    public ResponseEntity<ResponseObject> getproductallVariant() {
        return variantService.findAllVariant();
    }

    @GetMapping("/{variantId}")
    public ResponseEntity<ResponseObject> getproductallVariant(@PathVariable String variantId) {
        ProductVariant optional = (ProductVariant) variantService
                .findVariantById(Engine.convertString(variantId)).getBody().getData();
        return new ResponseEntity<>(new ResponseObject(200, "Oke",
                0,
                variantService.convertObject(optional)),
                HttpStatus.OK);
    }

    @GetMapping("/productid/{variantId}")
    public ResponseEntity<ResponseObject> getproductallVariantByProductId(@PathVariable String variantId) {
        return variantService.findVariantByProductId(Engine.convertString(variantId));
    }
}
