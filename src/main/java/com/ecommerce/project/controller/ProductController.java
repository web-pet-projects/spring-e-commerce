package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/categories/{categoryId}/products")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO,
                                                 @PathVariable Long categoryId) {
        ProductDTO savedProductDTO = productService.createProduct(categoryId, productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductDTO);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer page,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer limit,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        ProductResponse productResponse = productService.findAllProducts(page, limit, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                                 @RequestParam Integer page,
                                                                 @RequestParam Integer limit,
                                                                 @RequestParam String sortBy,
                                                                 @RequestParam String sortOrder) {
        ProductResponse productResponse = productService.searchByCategory(categoryId, page, limit, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                @RequestParam Integer page,
                                                                @RequestParam Integer limit,
                                                                @RequestParam String sortBy,
                                                                @RequestParam String sortOrder) {
        ProductResponse productResponse = productService.searchByKeyword(keyword, page, limit, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId,
                                                    @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProductDTO = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        ProductDTO deletedProductDTO = productService.removeProduct(productId);
        return new ResponseEntity<>(deletedProductDTO, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateImage(@PathVariable Long productId,
                                                  @RequestBody MultipartFile image) {
        ProductDTO updatedProductDTO = productService.updateImage(productId, image);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }
}
