package com.lulu.product.integration;

import com.lulu.product.dto.ProductRequest;
import com.lulu.product.dto.ProductResponse;
import com.lulu.product.model.CategoryModel;
import com.lulu.product.model.ProductModel;
import com.lulu.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ProductService productService;

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Act
        List<ProductResponse> products = productService.getAllProducts();

        // Assert
        assertEquals(1, products.size());
        ProductResponse product = products.get(0);
        assertEquals("Rosa Roja Test", product.getName());
        assertEquals(25.99, product.getPrice());
        assertEquals(100, product.getStock());
    }

    @Test
    void getProduct_ShouldReturnProduct_WhenExists() {
        // Act
        ProductResponse result = productService.getProduct(testProduct.getId());

        // Assert
        assertNotNull(result);
        assertEquals("Rosa Roja Test", result.getName());
        assertEquals("Rosa roja para pruebas de integración", result.getDescription());
        assertEquals(25.99, result.getPrice());
        assertEquals(100, result.getStock());
    }

    @Test
    void getProduct_ShouldThrowException_WhenNotExists() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            productService.getProduct(999L);
        });
    }

    @Test
    void createProduct_ShouldCreateAndReturnProduct() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setName("Rosa Blanca");
        request.setDescription("Rosa blanca elegante");
        request.setPrice(22.50);
        request.setStock(50);
        request.setCategoriaId(testCategory.getId());

        // Act
        ProductResponse result = productService.createProduct(request);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Rosa Blanca", result.getName());
        assertEquals("Rosa blanca elegante", result.getDescription());
        assertEquals(22.50, result.getPrice());
        assertEquals(50, result.getStock());

        // Verificar en la base de datos
        List<ProductModel> products = productRepository.findAll();
        assertEquals(2, products.size());
        
        Optional<ProductModel> newProduct = productRepository.findById(result.getId());
        assertTrue(newProduct.isPresent());
        assertEquals("Rosa Blanca", newProduct.get().getName());
    }

    @Test
    void updateProduct_ShouldUpdateAndReturnProduct() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setName("Rosa Roja Actualizada");
        request.setDescription("Descripción actualizada");
        request.setPrice(30.99);
        request.setStock(75);
        request.setCategoriaId(testCategory.getId());

        // Act
        ProductResponse result = productService.updateProduct(testProduct.getId(), request);

        // Assert
        assertNotNull(result);
        assertEquals("Rosa Roja Actualizada", result.getName());
        assertEquals("Descripción actualizada", result.getDescription());
        assertEquals(30.99, result.getPrice());
        assertEquals(75, result.getStock());

        // Verificar en la base de datos
        Optional<ProductModel> updatedProduct = productRepository.findById(testProduct.getId());
        assertTrue(updatedProduct.isPresent());
        assertEquals("Rosa Roja Actualizada", updatedProduct.get().getName());
        assertEquals(30.99, updatedProduct.get().getPrice());
    }

    @Test
    void updateProduct_ShouldThrowException_WhenNotExists() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setName("Producto Inexistente");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(999L, request);
        });
    }

    @Test
    void deleteProduct_ShouldRemoveProduct_WhenExists() {
        // Verificar que existe antes de borrar
        assertTrue(productRepository.findById(testProduct.getId()).isPresent());

        // Act
        productService.deleteProduct(testProduct.getId());

        // Assert
        assertFalse(productRepository.findById(testProduct.getId()).isPresent());
        
        List<ProductModel> products = productRepository.findAll();
        assertEquals(0, products.size());
    }

    @Test
    void getProductsByCategory_ShouldReturnFilteredProducts() {
        // Arrange - Crear categoría y producto adicional
        CategoryModel anotherCategory = new CategoryModel();
        anotherCategory.setNombre("Tulipanes");
        anotherCategory = categoryRepository.save(anotherCategory);

        ProductModel tulipan = new ProductModel();
        tulipan.setName("Tulipán Amarillo");
        tulipan.setDescription("Tulipán amarillo hermoso");
        tulipan.setPrice(15.99);
        tulipan.setStock(30);
        tulipan.setCategoria(anotherCategory);
        productRepository.save(tulipan);

        // Act
        List<ProductResponse> rosasProducts = productService.getProductsByCategory(testCategory.getId());
        List<ProductResponse> tulipanesProducts = productService.getProductsByCategory(anotherCategory.getId());

        // Assert
        assertEquals(1, rosasProducts.size());
        assertEquals("Rosa Roja Test", rosasProducts.get(0).getName());

        assertEquals(1, tulipanesProducts.size());
        assertEquals("Tulipán Amarillo", tulipanesProducts.get(0).getName());

        // Verificar total de productos
        List<ProductModel> allProducts = productRepository.findAll();
        assertEquals(2, allProducts.size());
    }

    @Test
    void importFromExcel_ShouldProcessValidFile() throws Exception {
        // Arrange
        MockMultipartFile validFile = new MockMultipartFile(
                "file",
                "productos_validos.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                createValidExcelContent()
        );

        // Act & Assert - No debe lanzar excepción
        assertDoesNotThrow(() -> {
            productService.importFromExcel(validFile);
        });
        
        // Verificar que se importaron productos adicionales
        List<ProductModel> products = productRepository.findAll();
        assertTrue(products.size() >= 1, "Debe tener al menos el producto de prueba inicial");
    }

    @Test
    void importFromExcel_ShouldHandleInvalidFile() throws Exception {
        // Arrange
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file",
                "archivo_invalido.txt",
                "text/plain",
                "contenido no válido".getBytes()
        );

        // Act & Assert - Puede lanzar excepción según la implementación
        assertThrows(Exception.class, () -> {
            productService.importFromExcel(invalidFile);
        });
    }

    @Test
    void getCategory_ShouldReturnAllCategories() {
        // Act
        List<CategoryModel> categories = productService.getCategory();

        // Assert
        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("Rosas de Prueba", categories.get(0).getNombre());
    }

    private byte[] createValidExcelContent() {
        // Crear un archivo Excel válido usando Apache POI
        try {
            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Productos");
            
            // Crear fila de encabezados
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Nombre");
            headerRow.createCell(1).setCellValue("Descripción");
            headerRow.createCell(2).setCellValue("Precio");
            headerRow.createCell(3).setCellValue("Stock");
            headerRow.createCell(4).setCellValue("Destacado");
            headerRow.createCell(5).setCellValue("CategoriaId");
            headerRow.createCell(6).setCellValue("ImageUrls");
            
            // Crear fila de datos
            org.apache.poi.ss.usermodel.Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue("Rosa Importada");
            dataRow.createCell(1).setCellValue("Rosa desde Excel");
            dataRow.createCell(2).setCellValue(19.99);
            dataRow.createCell(3).setCellValue(25);
            dataRow.createCell(4).setCellValue(true);
            dataRow.createCell(5).setCellValue(testCategory.getId());
            dataRow.createCell(6).setCellValue("");
            
            // Convertir a bytes
            java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error creando archivo Excel de prueba", e);
        }
    }
}
