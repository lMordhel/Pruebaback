package com.lulu.product.service;

import com.lulu.product.dto.ProductRequest;
import com.lulu.product.dto.ProductResponse;
import com.lulu.product.exception.FileUploadException;
import com.lulu.product.exception.ProductNotFoundException;
import com.lulu.product.mapper.ProductMapper;
import com.lulu.product.model.CategoryModel;
import com.lulu.product.model.ProductModel;
import com.lulu.product.repository.CategoryRepository;
import com.lulu.product.repository.ProductRepository;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        logger.info("Creando producto: {}", request.getName());
        ProductModel product = productMapper.toEntity(request);
        ProductModel savedProduct = productRepository.save(product);
        logger.info("Producto creado exitosamente con ID: {}", savedProduct.getId());
        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        logger.info("Actualizando producto con ID: {}", id);
        ProductModel product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        productMapper.updateEntityFromRequest(product, request);
        ProductModel updatedProduct = productRepository.save(product);
        logger.info("Producto actualizado exitosamente: {}", id);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        logger.info("Eliminando producto con ID: {}", id);
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
        logger.info("Producto eliminado exitosamente: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        logger.debug("Buscando producto con ID: {}", id);
        ProductModel product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        logger.debug("Obteniendo todos los productos");
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryModel> getCategory() {
        logger.debug("Obteniendo todas las categorías");
        return categoryRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(Long categoriaId) {
        logger.debug("Obteniendo productos por categoría: {}", categoriaId);
        return productRepository.findByCategoriaId(categoriaId).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    private String getStringValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    private double getNumericValue(Cell cell) {
        if (cell == null) return 0;
        return switch (cell.getCellType()) {
            case NUMERIC -> cell.getNumericCellValue();
            case STRING -> {
                try {
                    yield Double.parseDouble(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    logger.warn("Error parseando número: {}", cell.getStringCellValue());
                    yield 0;
                }
            }
            default -> 0;
        };
    }

    private boolean getBooleanValue(Cell cell) {
        if (cell == null) return false;
        return switch (cell.getCellType()) {
            case BOOLEAN -> cell.getBooleanCellValue();
            case STRING -> {
                String value = cell.getStringCellValue().trim().toLowerCase();
                yield "true".equals(value) || "1".equals(value) || "sí".equals(value) || "si".equals(value);
            }
            case NUMERIC -> cell.getNumericCellValue() != 0;
            default -> false;
        };
    }

    @Override
    public void importFromExcel(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("El archivo Excel no puede estar vacío");
        }
        
        if (!isExcelFile(file)) {
            throw new FileUploadException("El archivo debe ser un archivo Excel (.xlsx o .xls)");
        }

        logger.info("Iniciando importación desde Excel: {}", file.getOriginalFilename());
        List<ProductModel> products = new ArrayList<>();
        int processedRows = 0;

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // saltar encabezado

                // Validar celdas críticas
                if (row.getCell(0) == null || getStringValue(row.getCell(0)).isEmpty()) {
                    logger.warn("Fila {} omitida: nombre vacío", row.getRowNum());
                    continue;
                }

                try {
                    String name = getStringValue(row.getCell(0));
                    String description = getStringValue(row.getCell(1));
                    double price = getNumericValue(row.getCell(2));
                    int stock = (int) getNumericValue(row.getCell(3));
                    boolean destacado = getBooleanValue(row.getCell(4));
                    long categoriaId = (long) getNumericValue(row.getCell(5));
                    String rawLinks = getStringValue(row.getCell(6));

                    // Validaciones básicas
                    if (price <= 0) {
                        logger.warn("Fila {} omitida: precio inválido {}", row.getRowNum(), price);
                        continue;
                    }
                    
                    if (stock < 0) {
                        logger.warn("Fila {} omitida: stock negativo {}", row.getRowNum(), stock);
                        continue;
                    }

                    List<String> urls = Arrays.stream(rawLinks.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toList());

                    ProductRequest request = new ProductRequest();
                    request.setName(name);
                    request.setDescription(description);
                    request.setPrice(price);
                    request.setStock(stock);
                    request.setDestacado(destacado);
                    request.setCategoriaId(categoriaId);
                    request.setImageUrls(urls);

                    ProductModel model = productMapper.toEntityFromLinks(request);
                    products.add(model);
                    processedRows++;
                    
                } catch (Exception e) {
                    logger.error("Error procesando fila {}: {}", row.getRowNum(), e.getMessage());
                    // Continuar con la siguiente fila en lugar de fallar completamente
                }
            }

            if (products.isEmpty()) {
                throw new FileUploadException("No se encontraron productos válidos en el archivo Excel");
            }

            productRepository.saveAll(products);
            logger.info("Importación completada: {} productos importados de {} filas procesadas", 
                       products.size(), processedRows);
        } catch (Exception e) {
            logger.error("Error durante la importación de Excel: {}", e.getMessage(), e);
            throw new FileUploadException("Error al procesar el archivo Excel: " + e.getMessage(), e);
        }
    }
    
    private boolean isExcelFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
            contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
            contentType.equals("application/vnd.ms-excel")
        );
    }
}