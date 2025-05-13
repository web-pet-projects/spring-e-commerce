package com.ecommerce.project.service;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductDTO createProduct(Long categoryId, ProductDTO productDTO);

    ProductResponse findAllProducts(Integer page, Integer limit, String sortBy, String sortOrder);

    ProductResponse searchByCategory(Long categoryId, Integer page, Integer limit, String sortBy, String sortOrder);

    ProductResponse searchByKeyword(String keyword, Integer page, Integer limit, String sortBy, String sortOrder);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    ProductDTO removeProduct(Long productId);

    ProductDTO updateImage(Long productId, MultipartFile image);
}
