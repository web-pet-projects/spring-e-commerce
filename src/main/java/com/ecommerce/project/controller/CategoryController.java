package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "page", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "limit", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.CATEGORY_SORT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.CATEGORY_SORT_DIR, required = false) String sortOrder) {
        CategoryResponse response = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = categoryService.addCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

//    @PutMapping("public/categories/{categoryId}")
//    public ResponseEntity<String> updateCategory(@PathVariable Long categoryId, @RequestBody Category category) {
//        try {
//            Category updatedCategory = categoryService.updateCategory(categoryId, category);
//            return new ResponseEntity<>("Updated successfully!", HttpStatus.OK);
//        } catch (ResponseStatusException e) {
//            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
//        }
//    }

    @PutMapping("public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId,
                                                      @Valid @RequestBody CategoryDTO categoryDTO) {
            CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);
            return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
            CategoryDTO categoryDTO = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }


}
