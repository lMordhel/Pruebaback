package com.lulu.product.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Categoría no encontrada con id: " + id);
    }
    
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
