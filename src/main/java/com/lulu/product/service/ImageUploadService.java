package com.lulu.product.service;

import com.lulu.product.exception.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ImageUploadService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;
    
    @Value("${app.upload.base-url:http://localhost:8080}")
    private String baseUrl;
    
    @Value("${app.upload.max-file-size:5242880}") // 5MB por defecto
    private long maxFileSize;
    
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
        "jpg", "jpeg", "png", "gif", "webp"
    );
    
    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
        "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    public String upload(MultipartFile file) {
        validateFile(file);
        
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String filename = UUID.randomUUID() + "." + extension;
            Path filePath = uploadPath.resolve(filename);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return baseUrl + "/uploads/" + filename;

        } catch (IOException e) {
            throw new FileUploadException("Error al guardar el archivo: " + e.getMessage(), e);
        }
    }
    
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("El archivo no puede estar vacío");
        }
        
        if (file.getSize() > maxFileSize) {
            throw new FileUploadException("El archivo excede el tamaño máximo permitido de " + (maxFileSize / 1024 / 1024) + "MB");
        }
        
        String contentType = file.getContentType();
        if (!ALLOWED_MIME_TYPES.contains(contentType)) {
            throw new FileUploadException("Tipo de archivo no permitido. Solo se permiten: " + String.join(", ", ALLOWED_MIME_TYPES));
        }
        
        String filename = file.getOriginalFilename();
        if (filename == null || filename.trim().isEmpty()) {
            throw new FileUploadException("El nombre del archivo no puede estar vacío");
        }
        
        String extension = getFileExtension(filename);
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new FileUploadException("Extensión de archivo no permitida. Solo se permiten: " + String.join(", ", ALLOWED_EXTENSIONS));
        }
    }
    
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new FileUploadException("El archivo debe tener una extensión válida");
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
    
    public boolean deleteFile(String imageUrl) {
        try {
            // Extraer el nombre del archivo de la URL
            String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            Path filePath = Paths.get(uploadDir, filename);
            
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new FileUploadException("Error al eliminar el archivo: " + e.getMessage(), e);
        }
    }
}
