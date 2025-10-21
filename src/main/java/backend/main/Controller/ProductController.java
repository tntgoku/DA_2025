package backend.main.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import backend.main.Config.Engine;
import backend.main.Config.LoggerE;
import backend.main.DTO.ProductSpecificationDTO;
import backend.main.DTO.Product.ProductDTO;
import backend.main.Model.Categories;
import backend.main.Model.ResponseObject;
import backend.main.Model.Product.ProductVariant;
import backend.main.Model.Product.Products;
import backend.main.Request.ProductRequest;
import backend.main.Request.VariantRequest;
import backend.main.Service.CategoryService;
import backend.main.Request.Json.ProductJson;
import backend.main.Service.SpecificationService;
import backend.main.Service.VariantService;
import backend.main.Service.ServiceImp.Product.ProductService;

import org.slf4j.LoggerFactory;
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

    private final CategoryService categoryService;
    @Autowired
    private ProductService service;
    @Autowired
    private VariantService variantService;
    // private SpecificationRepository specicifcationRepository;
    // private UnitController unitController;
    private final SpecificationService specificationService;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductController.class);

    // @Autowired
    // private CategoryRepository categoryRepository;
    ProductController(CategoryService categoryService, SpecificationService specificationService) {
        this.categoryService = categoryService;
        this.specificationService = specificationService;
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getproductall() {
        return service.findAll();
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<ResponseObject> getproductallByCateID(@PathVariable String id) {

        return service.findAllByCateId(Engine.convertString(id));
    }

    @PostMapping
    public ResponseEntity<ResponseObject> SaveProduct(@RequestBody ProductJson data) {
        ProductRequest pr = ConvertJsonProduct(data);
        Products a = convertRequest(pr);
        a.setId(null);
        // ResponseEntity<ResponseObject> response =
        // categoryService.getById(data.getCategoryId());
        logger.info("------------CREATE PRODUCT------------");
        logger.info("Post Client: {}", pr.toString());
        if (pr.getVariants() == null || pr.getVariants().isEmpty())
            logger.info("Nulll");
        return service.createNew(a);
        // return new ResponseEntity<>(
        // new ResponseObject(200, "data ProductRequest Object", 0, a.getCategory()),
        // HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findProductsByID(@PathVariable String id) {

        return service.findProductById(Engine.convertString(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateProducts(@PathVariable Integer id,
            @RequestBody ProductJson data) {
        Products a = convertRequest(ConvertJsonProduct(data));
        a.setId(id);
        logger.info("Dulieu ve Speciificatoin,{}", a.getSpecifications());
        List<ProductSpecificationDTO> listnew = new Gson().fromJson(a.getSpecifications(),
                new TypeToken<List<ProductSpecificationDTO>>() {
                }.getType());
        logger.info("Dulieu ve Speciificatoin,{}", listnew.size());
        Map<String, Object> map = new HashMap<>();
        if (a.getSpecifications() != null && !a.getSpecifications().isBlank()) {
            ResponseEntity<ResponseObject> updateResponse = service.update(a);
            if (updateResponse != null && updateResponse.getBody() != null) {
                ResponseObject responseBody = updateResponse.getBody();
                if (responseBody.getData() != null) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> responseMap = (Map<String, Object>) responseBody.getData();
                    map = responseMap;
                    map.put("Listnew", listnew);
                    ProductDTO result = (ProductDTO) map.get("Object");
                    if (result != null) {
                        result.setSpecifications(listnew);
                    }
                }
            }
        }
        return new ResponseEntity<>(new ResponseObject(200, "Oke", 1, map), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteProduct(@PathVariable Integer id) {
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
    public ResponseEntity<ResponseObject> deleteProducts(@RequestBody HashMap<String, List<Integer>> request) {
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
        tes.setProductType(data.getProductType());
        String makeslug = data.getSlug();

        if (makeslug == null || makeslug.trim().isEmpty()) { // Kiểm tra null HOẶC rỗng (sau khi loại bỏ khoảng trắng)
            // Nếu slug không tồn tại, tạo slug từ tên
            String newSlug = Engine.makeSlug(data.getName());
            tes.setSlug(newSlug);
            logger.debug(makeslug, "Make Slug With Name: {}", newSlug);
        } else {
            // Nếu slug đã tồn tại và hợp lệ
            // Giữ nguyên slug hiện có (tes.getSlug())
            tes.setSlug(makeslug); // Hoặc tes.setSlug(data.getSlug());
            logger.debug(makeslug, "Slug đã tồn tại, không thay đổi.");
        }
        List<VariantRequest> variantRequests = new ArrayList<>();
        data.getVariants().forEach(items -> {
            String idcolor = items.getIdColor().toString();
            if (items.getIdColor() == null) {
                logger.info("IDcolor Null");
                items.setIdColor(UUID.randomUUID().toString().substring(0, 8));
            }
            if (items.getVariantsStorage() != null && !items.getVariantsStorage().isEmpty()) {
                logger.info("List nay ko null nehihi");
                items.getVariantsStorage().forEach(storage -> {
                    VariantRequest test = new VariantRequest();
                    test.setColor(items.getColor());
                    test.setColorCode(idcolor);
                    test.setVariantId(storage.getVariantId());
                    test.setPrice(storage.getPrice());
                    test.setSale_price(storage.getSale_price());
                    test.setList_price(storage.getList_price());
                    test.setStorage(storage.getStorage());
                    test.setStock(storage.getStock());
                    logger.info("Color: {} , Storage: {} , Price: {} , List_Price: {} , Stock: {}", items.getColor(),
                            storage.getStorage(), storage.getPrice(), storage.getList_price(), storage.getStock());
                    variantRequests.add(test);
                    logger.info("Da them Storage vao voi Id la {}", storage.getVariantId());
                });
            } else {
                VariantRequest test = new VariantRequest();
                test.setColor(items.getColor());
                test.setColorCode(items.getIdColor());
                variantRequests.add(test);
                logger.info("Color moi :{}", items.getColor());
            }
        });
        tes.setVariants(variantRequests);
        tes.setSpecifications(ConvertListSpecification(data.getSpecifications(), data));
        logger.info("Length List Variant this: {}", variantRequests.size());
        logger.info("Length List Specfications this: {}",
                (data.getSpecifications() != null ? data.getSpecifications().size() : -1));
        logger.info("Size List Specfications this: {}", tes.getSpecifications().size());
        return tes;
    }

    private Products convertRequest(ProductRequest request) {
        Products a = new Products();
        a.setSlug(request.getSlug());

        ResponseEntity<ResponseObject> cateResp = categoryService.getById(request.getCategoryId());

        if (cateResp != null && cateResp.getBody() != null) {
            ResponseObject cateResponseBody = cateResp.getBody();
            if (cateResponseBody.getData() != null && cateResponseBody.getData() instanceof Categories) {
                Categories cate = (Categories) cateResponseBody.getData();
                if (cate != null) {
                    // Nếu category có parent (tức là là danh mục con)
                    if (cate.getParent() != null) {
                        // Lấy danh mục cha
                        ResponseEntity<ResponseObject> parentResp = categoryService.getById(cate.getParent());
                        if (parentResp != null && parentResp.getBody() != null) {
                            ResponseObject parentResponseBody = parentResp.getBody();
                            if (parentResponseBody.getData() != null
                                    && parentResponseBody.getData() instanceof Categories) {
                                Categories parentCate = (Categories) parentResponseBody.getData();
                                a.setCategory(parentCate); // gán ID danh mục cha
                                logger.info("Category con => gán về cha có ID = {}", parentCate.getId());
                            } else {
                                logger.warn("Không tìm thấy danh mục cha cho categoryId={}", cate.getId());
                                a.setCategory(cate); // fallback
                            }
                        } else {
                            logger.warn("Không tìm thấy danh mục cha cho categoryId={}", cate.getId());
                            a.setCategory(cate); // fallback
                        }
                    } else {
                        // Là danh mục cha
                        a.setCategory(cate);
                        logger.info("Danh mục cha => gán chính nó (ID={})", cate.getId());
                    }
                }
            }
        } else {
            logger.warn("Không tìm thấy category với id={}", request.getCategoryId());
        }

        a.setName(request.getName());
        a.setBrand(request.getBrand());
        a.setDescription(request.getDescription());
        Gson gson = new Gson();
        if (request.getSpecifications() == null && request.getSpecifications().isEmpty()) {
            a.setSpecifications(null);
        } else {
            String jsonspec = gson.toJson(request.getSpecifications());
            a.setSpecifications(jsonspec);
        }
        a.setIsActive(request.getIsActive());
        a.setIsFeatured(request.getIsFeatured());
        a.setProductType(request.getProductType());
        a.setModel(request.getModel());
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(request.getSpecifications());
            a.setSpecifications(json);
        } catch (Exception e) {
            logger.error("Lỗi chuyển sang Json của ProductRequest: {}", e.getMessage());
        }
        List<ProductVariant> list = new ArrayList<>();
        request.getVariants().forEach(item -> {
            list.add(variantService.convertVariantRequest(item, a));
        });
        a.setVariants(list);
        return a;
    }

    private List<ProductSpecificationDTO> ConvertListSpecification(
            List<ProductSpecificationDTO> requestList, ProductJson p) {
        List<ProductSpecificationDTO> listNew = new ArrayList<>();
        if (requestList == null || requestList.isEmpty())
            return listNew;
        for (ProductSpecificationDTO item : requestList) {
            listNew.add(specificationService.createorupdatespe(item));
        }

        return listNew;
    }
}
