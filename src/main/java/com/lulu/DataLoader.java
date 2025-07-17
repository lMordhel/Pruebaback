package com.lulu;

import com.lulu.auth.model.RolModel;
import com.lulu.auth.model.UserModel;
import com.lulu.auth.repository.RolRepository;
import com.lulu.auth.repository.UserRepository;
import com.lulu.product.model.CategoryModel;
import com.lulu.product.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Insertar categorías
        insertCategoryIfNotExists("Flores", "Flores frescas y variadas");
        insertCategoryIfNotExists("Ramos", "Ramos personalizados para toda ocasión");
        insertCategoryIfNotExists("Globos", "Globos de helio y decorativos");
        insertCategoryIfNotExists("Peluches", "Peluches suaves y adorables");
        
        // Insertar roles
        insertRolIfNotExists("ADMIN");
        insertRolIfNotExists("USER");
        
        // Insertar usuario administrador
        insertAdminUserIfNotExists();
    }

    private void insertCategoryIfNotExists(String nombre, String descripcion) {
        if (!categoryRepository.existsByNombre(nombre)) {
            CategoryModel category = new CategoryModel();
            category.setNombre(nombre);
            category.setDescripcion(descripcion);
            categoryRepository.save(category);
        }
    }
    
    private void insertRolIfNotExists(String tipoRol) {
        if (rolRepository.findByTipoRol(tipoRol).isEmpty()) {
            RolModel rol = new RolModel();
            rol.setTipoRol(tipoRol);
            rolRepository.save(rol);
        }
    }
    
    private void insertAdminUserIfNotExists() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            UserModel admin = new UserModel();
            admin.setNombre("Admin");
            admin.setApellidos("Sistema");
            admin.setTelefono("123456789");
            admin.setDni("12345678");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setCorreo("admin@floreria.com");
            admin.setEstado("ACTIVO");
            
            // Asignar rol ADMIN
            RolModel adminRole = rolRepository.findByTipoRol("ADMIN").orElse(null);
            if (adminRole != null) {
                admin.setRol(adminRole);
            }
            
            userRepository.save(admin);
            System.out.println("🔑 Usuario admin creado - Username: admin, Password: admin123");
        }
    }
}
