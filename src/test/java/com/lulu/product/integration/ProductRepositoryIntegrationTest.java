package com.lulu.product.integration;

import com.lulu.product.model.CategoryModel;
import com.lulu.product.model.ProductModel;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProductRepositoryIntegrationTest extends BaseIntegrationTest {

    @Test
    void findAll_ShouldReturnAllProducts() {
        // Act
        List<ProductModel> products = productRepository.findAll();

        // Assert
        assertEquals(1, products.size());
        ProductModel product = products.get(0);
        assertEquals("Rosa Roja Test", product.getName());
        assertEquals(25.99, product.getPrice());
        assertEquals(100, product.getStock());
        assertEquals("Rosas de Prueba", product.getCategoria().getNombre());
    }

    @Test
    void findById_ShouldReturnProduct_WhenExists() {
        // Act
        Optional<ProductModel> result = productRepository.findById(testProduct.getId());

        // Assert
        assertTrue(result.isPresent());
        ProductModel product = result.get();
        assertEquals("Rosa Roja Test", product.getName());
        assertEquals("Rosa roja para pruebas de integración", product.getDescription());
        assertEquals(25.99, product.getPrice());
        assertEquals(100, product.getStock());
        assertNotNull(product.getCategoria());
        assertEquals("Rosas de Prueba", product.getCategoria().getNombre());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        // Act
        Optional<ProductModel> result = productRepository.findById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void save_ShouldPersistNewProduct() {
        // Arrange
        ProductModel newProduct = new ProductModel();
        newProduct.setName("Rosa Blanca");
        newProduct.setDescription("Rosa blanca elegante");
        newProduct.setPrice(22.50);
        newProduct.setStock(50);
        newProduct.setDestacado(false);
        newProduct.setCategoria(testCategory);

        // Act
        ProductModel savedProduct = productRepository.save(newProduct);

        // Assert
        assertNotNull(savedProduct.getId());
        assertEquals("Rosa Blanca", savedProduct.getName());
        assertEquals(22.50, savedProduct.getPrice());
        assertEquals(50, savedProduct.getStock());
        assertFalse(savedProduct.getDestacado());

        // Verificar que se guardó en la base de datos
        List<ProductModel> allProducts = productRepository.findAll();
        assertEquals(2, allProducts.size());
    }

    @Test
    void save_ShouldUpdateExistingProduct() {
        // Arrange
        testProduct.setName("Rosa Roja Actualizada");
        testProduct.setPrice(35.99);
        testProduct.setStock(75);

        // Act
        ProductModel updatedProduct = productRepository.save(testProduct);

        // Assert
        assertEquals(testProduct.getId(), updatedProduct.getId());
        assertEquals("Rosa Roja Actualizada", updatedProduct.getName());
        assertEquals(35.99, updatedProduct.getPrice());
        assertEquals(75, updatedProduct.getStock());

        // Verificar que no se creó un nuevo producto
        List<ProductModel> allProducts = productRepository.findAll();
        assertEquals(1, allProducts.size());
    }

    @Test
    void deleteById_ShouldRemoveProduct() {
        // Verificar que existe antes de borrar
        assertTrue(productRepository.findById(testProduct.getId()).isPresent());

        // Act
        productRepository.deleteById(testProduct.getId());

        // Assert
        assertFalse(productRepository.findById(testProduct.getId()).isPresent());
        
        List<ProductModel> products = productRepository.findAll();
        assertEquals(0, products.size());
    }

    @Test
    void delete_ShouldRemoveProduct() {
        // Verificar que existe antes de borrar
        assertTrue(productRepository.findById(testProduct.getId()).isPresent());

        // Act
        productRepository.delete(testProduct);

        // Assert
        assertFalse(productRepository.findById(testProduct.getId()).isPresent());
        
        List<ProductModel> products = productRepository.findAll();
        assertEquals(0, products.size());
    }

    @Test
    void findByCategoriaId_ShouldReturnProductsOfCategory() {
        // Arrange - Crear categoría y producto adicional
        CategoryModel anotherCategory = new CategoryModel();
        anotherCategory.setNombre("Tulipanes");
        anotherCategory = categoryRepository.save(anotherCategory);

        ProductModel tulipan = new ProductModel();
        tulipan.setName("Tulipán Amarillo");
        tulipan.setDescription("Tulipán amarillo hermoso");
        tulipan.setPrice(15.99);
        tulipan.setStock(30);
        tulipan.setDestacado(false);
        tulipan.setCategoria(anotherCategory);
        productRepository.save(tulipan);

        // Act
        List<ProductModel> rosasProducts = productRepository.findByCategoriaId(testCategory.getId());
        List<ProductModel> tulipanesProducts = productRepository.findByCategoriaId(anotherCategory.getId());

        // Assert
        assertEquals(1, rosasProducts.size());
        assertEquals("Rosa Roja Test", rosasProducts.get(0).getName());
        assertEquals("Rosas de Prueba", rosasProducts.get(0).getCategoria().getNombre());

        assertEquals(1, tulipanesProducts.size());
        assertEquals("Tulipán Amarillo", tulipanesProducts.get(0).getName());
        assertEquals("Tulipanes", tulipanesProducts.get(0).getCategoria().getNombre());

        // Verificar total de productos
        List<ProductModel> allProducts = productRepository.findAll();
        assertEquals(2, allProducts.size());
    }

    @Test
    void count_ShouldReturnCorrectCount() {
        // Act
        long initialCount = productRepository.count();

        // Arrange - Crear producto adicional
        ProductModel newProduct = new ProductModel();
        newProduct.setName("Rosa Nueva");
        newProduct.setDescription("Rosa recién agregada");
        newProduct.setPrice(20.00);
        newProduct.setStock(25);
        newProduct.setDestacado(false);
        newProduct.setCategoria(testCategory);
        productRepository.save(newProduct);

        // Act
        long finalCount = productRepository.count();

        // Assert
        assertEquals(1, initialCount);
        assertEquals(2, finalCount);
    }

    @Test
    void existsById_ShouldReturnCorrectResult() {
        // Act & Assert
        assertTrue(productRepository.existsById(testProduct.getId()));
        assertFalse(productRepository.existsById(999L));
    }
}
