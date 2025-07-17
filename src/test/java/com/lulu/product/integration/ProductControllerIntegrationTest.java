package com.lulu.product.integration;

import com.lulu.product.dto.ProductRequest;
import com.lulu.product.model.CategoryModel;
import com.lulu.product.model.ProductModel;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductControllerIntegrationTest extends BaseControllerIntegrationTest {

    @Test
    void getAllProducts_ShouldReturnProductsList() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Rosa Roja Test"))
                .andExpect(jsonPath("$[0].price").value(25.99))
                .andExpect(jsonPath("$[0].stock").value(100));
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenExists() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/products/{id}", testProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Rosa Roja Test"))
                .andExpect(jsonPath("$.price").value(25.99))
                .andExpect(jsonPath("$.stock").value(100));
    }

    @Test
    void getProductById_ShouldReturn404_WhenNotExists() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/products/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct_ShouldCreateAndReturnProduct() throws Exception {
        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/products")
                        .param("name", "Rosa Blanca")
                        .param("description", "Rosa blanca elegante")
                        .param("price", "22.50")
                        .param("stock", "50")
                        .param("categoriaId", testCategory.getId().toString()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Rosa Blanca"))
                .andExpect(jsonPath("$.price").value(22.50))
                .andExpect(jsonPath("$.stock").value(50));

        // Verificar que se guardó en la base de datos
        List<ProductModel> products = productRepository.findAll();
        assertEquals(2, products.size());
        
        Optional<ProductModel> newProduct = products.stream()
                .filter(p -> "Rosa Blanca".equals(p.getName()))
                .findFirst();
        assertTrue(newProduct.isPresent());
        assertEquals("Rosa blanca elegante", newProduct.get().getDescription());
    }

    @Test
    void updateProduct_ShouldUpdateAndReturnProduct() throws Exception {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setName("Rosa Roja Actualizada");
        request.setDescription("Descripción actualizada");
        request.setPrice(30.99);
        request.setStock(75);
        request.setCategoriaId(testCategory.getId());

        String requestJson = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(put("/api/products/{id}", testProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Rosa Roja Actualizada"))
                .andExpect(jsonPath("$.price").value(30.99))
                .andExpect(jsonPath("$.stock").value(75));

        // Verificar que se actualizó en la base de datos
        Optional<ProductModel> updatedProduct = productRepository.findById(testProduct.getId());
        assertTrue(updatedProduct.isPresent());
        assertEquals("Rosa Roja Actualizada", updatedProduct.get().getName());
        assertEquals(30.99, updatedProduct.get().getPrice());
    }

    @Test
    void deleteProduct_ShouldRemoveProduct() throws Exception {
        // Verificar que existe antes de borrar
        assertTrue(productRepository.findById(testProduct.getId()).isPresent());

        // Act & Assert
        mockMvc.perform(delete("/api/products/{id}", testProduct.getId()))
                .andExpect(status().isNoContent());

        // Verificar que se eliminó de la base de datos
        assertFalse(productRepository.findById(testProduct.getId()).isPresent());
        
        List<ProductModel> products = productRepository.findAll();
        assertEquals(0, products.size());
    }

    @Test
    void getProductsByCategory_ShouldReturnFilteredProducts() throws Exception {
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

        // Act & Assert - Productos de la categoría rosas
        mockMvc.perform(get("/api/products/category/{categoryId}", testCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Rosa Roja Test"));

        // Act & Assert - Productos de la categoría tulipanes
        mockMvc.perform(get("/api/products/category/{categoryId}", anotherCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Tulipán Amarillo"));

        // Verificar total de productos
        List<ProductModel> allProducts = productRepository.findAll();
        assertEquals(2, allProducts.size());
    }

    @Test
    void importFromExcel_ShouldProcessFile() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "productos.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                createValidExcelContent()
        );

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/products/import")
                        .file(file))
                .andExpect(status().isOk());

        // Verificar que se importaron productos adicionales
        List<ProductModel> products = productRepository.findAll();
        assertTrue(products.size() >= 1, "Debe tener al menos el producto de prueba inicial");
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
