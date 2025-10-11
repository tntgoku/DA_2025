package backend.main.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import backend.main.Config.Engine;
import backend.main.Config.LoggerE;
import backend.main.Model.Categories;
import backend.main.Model.ProductSpecfication;
import backend.main.Model.ResponseObject;
import backend.main.Model.Specification;
import backend.main.Model.Unit;
import backend.main.Model.Product.ProductVariant;
import backend.main.Model.Product.Products;
import backend.main.Repository.CategoryRepository;
import backend.main.Request.ProductRequest;
import backend.main.Request.VariantRequest;
import backend.main.Request.Json.ProductJson;
import backend.main.Service.ProductService;
import backend.main.Service.VariantService;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService service;
    @Autowired
    private VariantService variantService;
    @Autowired
    private ProductSpecificationController specificationController;
    @Autowired
    private UnitController unitController;
    private final Logger logger = LoggerE.logger;

    @GetMapping
    public ResponseEntity<ResponseObject> getproductall() {
        return service.findAll();
    }

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping
    public ResponseEntity<ResponseObject> SaveProduct(@RequestBody ProductJson data) {
        ProductRequest pr = ConvertJsonProduct(data);
        Products a = convertRequest(ConvertJsonProduct(data));
        Optional<Categories> test = categoryRepository.findById(pr.getCategoryId().intValue());

        // ResponseEntity<ResponseObject> response =
        // categoryService.getById(data.getCategoryId());
        logger.info("------------CREATE PRODUCT------------");
        logger.info("Post Client: " + pr.toString());
        if (pr.getVariants() == null || pr.getVariants().isEmpty())
            logger.info("Nulll");
        // return service.createNew(a);
        return new ResponseEntity<>(
                new ResponseObject(200, "data ProductRequest Object", 0, a.getCategory()),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findProductsByID(@PathVariable String id) {

        return service.findProductById(Engine.convertString(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateProducts(@PathVariable Integer id,
            @RequestBody ProductJson data) {
        ProductRequest pr = ConvertJsonProduct(data);
        Products a = convertRequest(ConvertJsonProduct(data));
        a.setId(id);
        // a.setId(Engine.convertString(id));
        return service.update(a);
        // logger.info("product" + a.getId() + ", " + a.getName() + "," +
        // a.getDescription() + ", " + a.getSlug()
        // + "," + a.getSpecifications());
        // return new ResponseEntity<>(new ResponseObject(200, "Product Request Object",
        // 0, pr),
        // HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteCategory(@PathVariable Integer id) {
        return service.delete(id);
        // if (id == null) {
        // return new ResponseEntity<>(new ResponseObject(404, "Not found data", 0, id),
        // HttpStatus.NOT_FOUND);
        // }
        // logger.info("Remove Product with ID: " + id);
        // return new ResponseEntity<>(new ResponseObject(200, "data ", 0, id),
        // HttpStatus.OK);
    }

    @DeleteMapping("/removemultiple")
    public ResponseEntity<ResponseObject> deleteCategories(@RequestBody HashMap<String, List<Integer>> request) {
        List<Integer> ids = request.get("ids");

        return service.deletes(ids);
    }

    @GetMapping("/featured")
    public ResponseEntity<ResponseObject> getproductallfeature() {
        return service.findAllisFeature();
    }

    @GetMapping("/inventory")
    public ResponseEntity<ResponseObject> getproductallInventory() {
        return variantService.findAllInventory();
    }

    @GetMapping("/img")
    public ResponseEntity<ResponseObject> get1productallVariant() {
        return service.listimg();
    }

    private ProductRequest ConvertJsonProduct(ProductJson data) {
        ProductRequest tes = new ProductRequest();
        tes.setBrand(data.getBrand());
        tes.setId(data.getId() == null ? 0 : data.getId());
        tes.setCategoryId(data.getCategory() == null ? 0 : data.getCategory());
        tes.setDescription(data.getDescription());
        tes.setName(data.getName());
        tes.setIsActive(data.getIsActive());
        tes.setIsFeatured(data.getIsFeatured());
        tes.setIsHot(data.getIsHot());
        String makeslug = data.getSlug();

        if (makeslug == null || makeslug.trim().isEmpty()) { // Kiểm tra null HOẶC rỗng (sau khi loại bỏ khoảng trắng)
            // Nếu slug không tồn tại, tạo slug từ tên
            String newSlug = Engine.makeSlug(data.getName());
            tes.setSlug(newSlug);
            logger.info("Make Slug With Name: " + newSlug);
        } else {
            // Nếu slug đã tồn tại và hợp lệ
            // Giữ nguyên slug hiện có (tes.getSlug())
            tes.setSlug(makeslug); // Hoặc tes.setSlug(data.getSlug());
            logger.info("Slug already exists, no change made.");
        }

        // Kiểm tra độ dài an toàn sau khi đã chắc chắn tes.getSlug() không null
        String finalSlug = tes.getSlug();
        if (finalSlug != null) {
            logger.info("Final Slug: " + finalSlug + ", Length: " + finalSlug.length());
        } else {
            logger.warning("Final Slug is still null/empty!"); // Trường hợp lỗi nếu data.getName() trả về null
        }
        List<VariantRequest> variantRequests = new ArrayList<>();
        data.getVariants().forEach(items -> {
            if (items.getVariantsStorage() != null && !items.getVariantsStorage().isEmpty()) {
                logger.info("List nay ko null nehihi");
                items.getVariantsStorage().forEach(storage -> {
                    VariantRequest test = new VariantRequest();
                    test.setColor(items.getColor());
                    test.setVariantId(storage.getVariantId());
                    test.setPrice(storage.getPrice());
                    test.setList_price(storage.getList_price());
                    test.setStorage(storage.getStorage());
                    logger.info("Color: " + items.getColor() + "\\sStorage: " + storage.getStorage() + "\\sPrice: "
                            + storage.getPrice() + "\\sList_Price: " + storage.getList_price() + "\\sStock:"
                            + (storage.getStock() == null ? 0 : storage.getStock()));
                    variantRequests.add(test);
                    logger.info("Da them Storage vao voi Id la " + storage.getVariantId());

                });
            } else {
                VariantRequest test = new VariantRequest();
                test.setColor(items.getColor());
                variantRequests.add(test);
                logger.info(("Color moi :" + items.getColor()));
            }
        });
        tes.setVariants(variantRequests);
        tes.setSpecifications(ConvertListSpecification(data.getSpecifications(), data));
        logger.info("Length List Variant this: " + variantRequests.size());
        logger.info(
                "Length List Specfications this: "
                        + (data.getSpecifications() != null ? data.getSpecifications().size() : -1));
        return tes;
    }

    private Products convertRequest(ProductRequest request) {
        Products a = new Products();
        a.setSlug(request.getSlug());
        a.setCategory(categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));

        a.setName(request.getName());
        a.setBrand(request.getBrand());
        a.setDescription(request.getDescription());
        a.setIsActive(request.getIsActive());
        a.setIsFeatured(request.getIsFeatured());
        a.setProductType(request.getProductType());
        a.setModel(request.getModel());
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(request.getSpecifications());
            a.setSpecifications(json);
        } catch (Exception e) {
            logger.warning("Lỗi chuyển sang Json của ProductRequest: " + e.getMessage());
        }
        List<ProductVariant> list = new ArrayList<>();
        request.getVariants().forEach(item -> {
            list.add(variantService.convertVariantRequest(item, a));
        });
        a.setVariants(list);
        return a;
    }

    private List<ProductSpecfication> ConvertListSpecification(
            List<ProductSpecfication> requestList, ProductJson p) {

        List<ProductSpecfication> listNew = new ArrayList<>();

        if (requestList == null || requestList.isEmpty())
            return listNew;

        // Lấy danh sách specification trong DB
        @SuppressWarnings("unchecked")
        List<Specification> dbSpecs = (List<Specification>) specificationController
                .getproductall()
                .getBody()
                .getData();

        // Duyệt qua từng item
        for (ProductSpecfication item : requestList) {
            logger.info("Request Spec: " + item.toString());
            // Tìm spec tương ứng trong DB theo tên (value = name)
            Specification matchedSpec = dbSpecs.stream()
                    .filter(s -> s.getName() != null && s.getName().equalsIgnoreCase(item.getValue()))
                    .findFirst()
                    .orElse(null);
            // Tạo object mới
            ProductSpecfication newSpec = new ProductSpecfication();
            newSpec.setId(null); // luôn null khi thêm mới
            newSpec.setProductId(p.getId()); // gán id sản phẩm
            newSpec.setValue(item.getValue());
            newSpec.setLabel(item.getLabel());

            // Nếu tìm thấy spec tương ứng trong DB thì gán specId + unitName
            if (matchedSpec != null) {
                newSpec.setSpecId(matchedSpec.getId());
                newSpec.setUnitName(getUnitNameById(matchedSpec.getUnitId())); // lấy tên đơn vị
            } else {
                newSpec.setSpecId(null);
                newSpec.setUnitName(item.getUnitName());
            }
            logger.info(("NewSpecfication :" + newSpec.toString()));
            listNew.add(newSpec);
        }

        return listNew;
    }

    private String getUnitNameById(Integer unitId) {
        if (unitId == null)
            return null;
        @SuppressWarnings("unchecked")
        List<Unit> units = (List<Unit>) unitController.getAll().getBody().getData();
        Unit unit = units.stream()
                .filter(u -> u.getKey().equals(unitId))
                .findFirst()
                .orElse(null);

        return unit != null ? unit.getValue() : null;
    }

}
