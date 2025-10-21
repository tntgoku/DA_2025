package backend.main.Controller;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.main.Model.Categories;
import backend.main.Model.ResponseObject;
import backend.main.Request.CateRequest;
import backend.main.Service.CategoryService;
import java.util.*;

@RestController
@RequestMapping("/api/cate")
public class CategoryController {
    @Autowired
    private CategoryService service;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @GetMapping
    public ResponseEntity<ResponseObject> getproductall() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getCategoryById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @GetMapping("/slug/{id}")
    public ResponseEntity<ResponseObject> getCategoryBySlug(@PathVariable String id) {
        return service.getBySlug(id);
    }
    
    @PostMapping
    public ResponseEntity<ResponseObject> createCategory(@RequestBody CateRequest data) {
        Categories a = new Categories();
        logger.info("Post Client: " + data);
        a.setName(data.getName());
        a.setIsActive(data.getActive());
        a.setParent(data.getParentId());
        a.setDisplayOrder(data.getDisplayOrder());
        logger.info("Convenrt Object Client Post: {}" , a);
        return service.createNew(a);
        // return new ResponseEntity<>(new ResponseObject(200, "data", 0, data),
        // HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateCategory(@PathVariable String id,
            @RequestBody CateRequest data) {

        Categories a = new Categories();
        logger.info("Post Client: {}" , data);
        a.setName(data.getName());
        a.setIsActive(data.getActive());
        a.setParent(data.getParentId());
        a.setDisplayOrder(data.getDisplayOrder());
        a.setId(Integer.parseInt(id));
        a.setSlug(data.getSlug());
        return service.update(a);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteCategory(@PathVariable Integer id) {
        return service.delete(id);
    }

    @DeleteMapping("/removemultiple")
    public ResponseEntity<ResponseObject> deleteCategories(@RequestBody HashMap<String, List<Integer>> request) {
        List<Integer> ids = request.get("ids");

        return service.deletes(ids);
    }

    @GetMapping("/hierarchy")
    public ResponseEntity<ResponseObject> getCategoryHierarchy() {
        return service.getCategoryHierarchy();
    }

    @GetMapping("/breadcrumb/{slug}")
    public ResponseEntity<ResponseObject> getBreadcrumbBySlug(@PathVariable String slug) {
        return service.getBreadcrumbBySlug(slug);
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<ResponseObject> getCategoriesByParent(@PathVariable Integer parentId) {
        return service.getCategoriesByParent(parentId);
    }

    @GetMapping("/seo-url/{parentSlug}/{childSlug}")
    public ResponseEntity<ResponseObject> getCategoryBySeoUrl(
            @PathVariable String parentSlug, 
            @PathVariable String childSlug) {
        return service.getCategoryBySeoUrl(parentSlug, childSlug);
    }

    @GetMapping("/seo-url/{parentSlug}")
    public ResponseEntity<ResponseObject> getCategoryByParentSlug(@PathVariable String parentSlug) {
        return service.getCategoryByParentSlug(parentSlug);
    }

    // @GetMapping("/search")
    // public ResponseEntity<ResponseObject> searchCategories(
    //         @RequestParam(required = false) String keyword,
    //         @RequestParam(required = false) Integer parentId,
    //         @RequestParam(required = false) Boolean isActive,
    //         @RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "10") int size) {
    //     return service.searchCategories(keyword, parentId, isActive, page, size);
    // }
}
