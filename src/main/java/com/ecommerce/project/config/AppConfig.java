package com.ecommerce.project.config;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce API")
                        .version("1.0")
                        .description("API documentation for your Spring Boot e-commerce project")
                        .contact(new Contact()
                                .name("Quang Vu")
                                .email("quang@example.com")
                        )
                );
    }

    @Bean
    public CommandLineRunner initData1(CategoryRepository categoryRepository, ProductRepository productRepository) {
        return args -> {
            List< Category> categories = List.of(
                    new Category("Home Appliances"),
                    new Category("Sport"),
                    new Category("Fashion")
            );
            categoryRepository.saveAll(categories);
            List<Product> products = List.of(
                    new Product("Robot 1", 10.0, 90, 250.0, categories.getFirst()),
                    new Product("Robot 2", 10.0, 40, 200.0, categories.getFirst()),
                    new Product("Robot 3", 5.0, 10, 1000.0, categories.getFirst()),
                    new Product("Shirt", 10.0, 90, 25.0, categories.getLast()),
                    new Product("Badminton Racket", 0.0, 40, 300.0, categories.get(2))
            );
            productRepository.saveAll(products);
        };
    }
}
