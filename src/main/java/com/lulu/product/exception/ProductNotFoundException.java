package com.lulu.product.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Producto no encontrado con id: " + id);
    }
    
    public ProductNotFoundException(String message) {
        super(message);
    }
}
