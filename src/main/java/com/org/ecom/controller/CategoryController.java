package com.org.ecom.controller;

import com.org.ecom.entity.category.Category;
import com.org.ecom.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    //API for Category Home
    @GetMapping("/home")
    public ResponseEntity<String> categoryHome() {
        String msg = "Category Home";
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    //API for Adding a New Category only Accessible by ADMIN
    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@RequestParam(name = "categoryName", required = true) String categoryName,
                                         @RequestParam(name = "parentId", required = false) Long parentCategoryId) {

        return categoryService.addCategory(categoryName, parentCategoryId);

    }

    //API for View Category only Accessible by ADMIN
    @GetMapping("/viewCategoryById")
    public ResponseEntity<?> viewCategory(@RequestParam(name = "categoryId", required = true) Long categoryId) {
        return categoryService.viewCategory(categoryId);
    }

    //API for Viewing All Category only Accessible by ADMIN
    @GetMapping("/viewAllCategory")
    public ResponseEntity<List<Category>> viewAllCategory(@RequestParam(name = "pageNo", required = false, defaultValue = "0") Integer pageNo,
                                                          @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                          @RequestParam(name = "sortBy", required = false, defaultValue = "category_id") String sortBy,
                                                          @RequestParam(name = "orderBy", required = false, defaultValue = "category_id") String orderBy) {

        return categoryService.viewAllCategory(pageNo, pageSize, sortBy, orderBy);
    }


    //API For adding Metadata Field of a Category
    @PostMapping("/addMetadataField")
    public ResponseEntity<?> addMetadata(@RequestParam("metadataField") String metadataField) {
        return categoryService.addMetadataField(metadataField);
    }

    //API for Viewing the Metadata Field of a Category
    @GetMapping("/viewMetadataField")
    public ResponseEntity<?> viewMetadataField() {
        return categoryService.viewMetadataField();
    }

    //API for updating a Category
    @PutMapping("/updateCategory")
    public ResponseEntity<?> updateCategory(@RequestParam("categoryId") Long id,
                                            @RequestParam("categoryName") String name) {
        return categoryService.updateCategory(id, name);
    }

    //API for Adding the Values of Metadata Field of a Category
    @PostMapping("/addMetadataFieldValues")
    public ResponseEntity<?> addMetadataFieldValues(@RequestParam("categoryId") Long categoryId,
                                                    @RequestParam("metadataFieldId") Long metadataFieldId,
                                                    @RequestParam("values") Set<String> values) {
        return categoryService.addCategoryMetadataFieldValues(categoryId, metadataFieldId, values);
    }

    //API for updating the values of Metadata Field Values of a Metadata of a Category
    @PutMapping("/updateMetadataFieldValues")
    public ResponseEntity<?> updateMetadataFieldValues(@RequestParam("categoryId") Long categoryId,
                                                       @RequestParam("metadataFieldId") Long metadataFieldId,
                                                       @RequestParam("values") Set<String> values) {
        return categoryService.updateCategoryMetadataFieldValues(categoryId, metadataFieldId, values);
    }
}
