package com.lulu.product.controller;

import com.lulu.product.dto.ProductRequest;
import com.lulu.product.dto.ProductResponse;
import com.lulu.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "API para gestión de productos")
public class ProductController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    
    @Autowired
    private ProductService productService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Crear producto", description = "Crea un nuevo producto con imágenes")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @ModelAttribute ProductRequest request) {
        logger.info("Creando producto: {}", request.getName());
        ProductResponse product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente")
    @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "ID del producto") @PathVariable Long id, 
            @Valid @RequestBody ProductRequest request) {
        logger.info("Actualizando producto ID: {}", id);
        ProductResponse product = productService.updateProduct(id, request);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto por su ID")
    @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        logger.info("Eliminando producto ID: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto", description = "Obtiene un producto por su ID")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    public ResponseEntity<ProductResponse> getProduct(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        logger.debug("Obteniendo producto ID: {}", id);
        ProductResponse product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    @Operation(summary = "Listar productos", description = "Obtiene todos los productos")
    @ApiResponse(responseCode = "200", description = "Lista de productos")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        logger.debug("Obteniendo todos los productos");
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{id}")
    @Operation(summary = "Productos por categoría", description = "Obtiene productos filtrados por categoría")
    @ApiResponse(responseCode = "200", description = "Lista de productos de la categoría")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @Parameter(description = "ID de la categoría") @PathVariable("id") Long id) {
        logger.debug("Obteniendo productos por categoría: {}", id);
        List<ProductResponse> products = productService.getProductsByCategory(id);
        return ResponseEntity.ok(products);
    }
    
    @PostMapping("/import")
    @Operation(summary = "Importar desde Excel", description = "Importa productos masivamente desde un archivo Excel")
    @ApiResponse(responseCode = "200", description = "Productos importados exitosamente")
    @ApiResponse(responseCode = "400", description = "Error en el archivo Excel")
    public ResponseEntity<String> importFromExcel(
            @Parameter(description = "Archivo Excel con productos") 
            @RequestParam("file") MultipartFile file) {
        try {
            logger.info("Iniciando importación desde Excel: {}", file.getOriginalFilename());
            productService.importFromExcel(file);
            return ResponseEntity.ok("Archivo importado correctamente");
        } catch (Exception e) {
            logger.error("Error al importar desde Excel: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al importar: " + e.getMessage());
        }
    }
}
