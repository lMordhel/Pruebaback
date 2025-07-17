package com.lulu.product.integration;

import com.lulu.auth.model.UserModel;
import com.lulu.auth.repository.UserRepository;
import com.lulu.product.model.CategoryModel;
import com.lulu.product.model.ProductModel;
import com.lulu.product.repository.CategoryRepository;
import com.lulu.product.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class BaseIntegrationTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected UserRepository userRepository;

    protected CategoryModel testCategory;
    protected ProductModel testProduct;
    protected UserModel testUser;

    @BeforeEach
    void baseSetUp() {
        // Limpiar base de datos
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        // Crear datos de prueba base
        createTestCategory();
        createTestProduct();
        createTestUser();
    }

    protected void createTestCategory() {
        testCategory = new CategoryModel();
        testCategory.setNombre("Rosas de Prueba");
        testCategory.setDescripcion("Categoría para pruebas de integración");
        testCategory = categoryRepository.save(testCategory);
    }

    protected void createTestProduct() {
        testProduct = new ProductModel();
        testProduct.setName("Rosa Roja Test");
        testProduct.setDescription("Rosa roja para pruebas de integración");
        testProduct.setPrice(25.99);
        testProduct.setStock(100);
        testProduct.setCategoria(testCategory);
        testProduct = productRepository.save(testProduct);
    }

    protected void createTestUser() {
        testUser = new UserModel();
        testUser.setCorreo("test@test.com");
        testUser.setPassword("password123");
        testUser.setNombre("Test");
        testUser.setApellidos("User");
        testUser.setUsername("testuser");
        testUser = userRepository.save(testUser);
    }
}
